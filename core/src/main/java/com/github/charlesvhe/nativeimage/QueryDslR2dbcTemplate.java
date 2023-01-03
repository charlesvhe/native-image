package com.github.charlesvhe.nativeimage;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.lang.Nullable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class QueryDslR2dbcTemplate {
    protected final R2dbcEntityOperations entityOperations;
    protected final SQLQueryFactory sqlQueryFactory;

    public QueryDslR2dbcTemplate(R2dbcEntityOperations entityOperations, SQLQueryFactory sqlQueryFactory) {
        this.entityOperations = entityOperations;
        this.sqlQueryFactory = sqlQueryFactory;
    }


    // 传递columns以便兼容join
    public static Expression[] buildColumn(@Nullable String fields, Collection<Path<?>> columns) {
        List<String> fieldList = StringUtils.commaDelimitedListToSet(fields)
                .stream().map(String::trim)
                .toList();

        List<Path<?>> columnList = columns.stream()
                .filter(column -> fieldList.isEmpty() || fieldList.contains(column.getMetadata().getName()))
                .collect(Collectors.toList());


        return columnList.toArray(new Expression[columnList.size()]);
    }

    @SneakyThrows
    public static <F> Predicate[] buildWhere(@Nullable F filter, Collection<Path<?>> columns, ExampleMatcher.StringMatcher stringMatcher) {
        if (filter == null) {
            return new Predicate[0];
        }

        ArrayList<Predicate> predicateList = new ArrayList<>();
        for (Path column : columns) {
            if (column instanceof StringPath) {
                StringPath path = (StringPath) column;
                String property = path.getMetadata().getName();

                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(filter.getClass(), property);
                if (null != pd) {
                    String value = (String) pd.getReadMethod().invoke(filter);
                    if (null != value) {
                        if (stringMatcher.equals(ExampleMatcher.StringMatcher.CONTAINING)) {
                            predicateList.add(path.contains(value));
                        } else if (stringMatcher.equals(ExampleMatcher.StringMatcher.EXACT)) {
                            predicateList.add(path.eq(value));
                        } else {
                            throw new IllegalArgumentException("StringMatcher not support " + stringMatcher.toString());
                        }
                    }
                }
            } else if (column instanceof NumberPath) {
                NumberPath path = (NumberPath) column;
                String property = path.getMetadata().getName();

                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(filter.getClass(), property);
                if (null != pd) {
                    Object value = pd.getReadMethod().invoke(filter);
                    if (null != value) {
                        predicateList.add(path.eq(value));
                    }
                }
            }
        }

        return predicateList.toArray(new Predicate[predicateList.size()]);
    }

    public static OrderSpecifier[] buildOrder(@Nullable String sort, Collection<Path<?>> columns) {
        if (!StringUtils.hasText(sort)) {
            return new OrderSpecifier[0];
        }

        ArrayList<OrderSpecifier> orderList = new ArrayList<>();
        String[] fields = StringUtils.commaDelimitedListToStringArray(sort);
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();
            boolean asc = true;
            if (field.charAt(0) == '-') {
                field = field.substring(1);
                asc = false;
            }
            for (Path column : columns) {
                if (field.equals(column.getMetadata().getName()) && column instanceof ComparableExpressionBase) {
                    ComparableExpressionBase comparableColumn = (ComparableExpressionBase) column;
                    if (asc) {
                        orderList.add(comparableColumn.asc());
                    } else {
                        orderList.add(comparableColumn.desc());
                    }
                }
            }
        }

        return orderList.toArray(new OrderSpecifier[orderList.size()]);
    }

    public static ArrayList<String> toColumnNameList(List<Expression<?>> expressionList) {
        ArrayList<String> columnList = new ArrayList<>();
        for (Expression<?> exp : expressionList) {
            if (exp instanceof Path) {
                Path pathArg = (Path) exp;
                columnList.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, pathArg.getMetadata().getName()));
            } else if (exp instanceof Operation) {
                Operation op = (Operation) exp;
                if (op.getOperator().equals(Ops.ALIAS)) {
                    Path pathArg = (Path) op.getArg(1);
                    columnList.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, pathArg.getMetadata().getName()));
                } else {
                    throw new IllegalArgumentException("not support " + exp);
                }
            } else {
                throw new IllegalArgumentException("not support " + exp);
            }
        }
        return columnList;
    }

    protected DatabaseClient.GenericExecuteSpec buildGenericExecuteSpec(List<SQLBindings> sqlBindingsList) {
        return this.entityOperations.getDatabaseClient()
                .sql(sqlBindingsList.get(0).getSQL())
                .filter(stm -> {
                    int bindCount = 0;
                    for (SQLBindings sqlBindings : sqlBindingsList) {
                        int bindIndex = 0;
                        for (Object obj : sqlBindings.getNullFriendlyBindings()) {
                            stm = stm.bind(bindIndex, obj);
                            bindIndex++;
                        }
                        bindCount++;
                        // batch bind
                        if (bindCount < sqlBindingsList.size()) {
                            stm = stm.add();
                        }
                    }
                    return stm;
                });
    }

    // 参考 com.querydsl.sql.AbstractSQLQuery AbstractSQLInsertClause AbstractSQLDeleteClause AbstractSQLUpdateClause
    public <O> Flux<O> select(Function<SQLQuery<?>, SQLQuery<O>> build) {
        SQLQuery<O> query = build.apply(sqlQueryFactory.query());

        FactoryExpressionBase<O> feb = (FactoryExpressionBase<O>) query.getMetadata().getProjection();
        List<Expression<?>> febArgs = feb.getArgs();

        DatabaseClient.GenericExecuteSpec spec = buildGenericExecuteSpec(Collections.singletonList(query.getSQL()));

        return spec.map(row -> {
            Object[] args = new Object[febArgs.size()];
            int febIndex = 0;
            for (Expression<?> febArg : febArgs) {
                args[febIndex] = row.get(febIndex, febArg.getType());
                febIndex++;
            }

            return feb.newInstance(args);
        }).all();
    }

    public Mono<Long> insert(RelationalPath<?> table, Function<SQLInsertClause, SQLInsertClause> build) {
        return buildGenericExecuteSpec(build.apply(sqlQueryFactory.insert(table)).getSQL())
                .fetch()
                .rowsUpdated();
    }

    public Mono<Long> delete(RelationalPath<?> table, Function<SQLDeleteClause, SQLDeleteClause> build) {
        return buildGenericExecuteSpec(build.apply(sqlQueryFactory.delete(table)).getSQL())
                .fetch()
                .rowsUpdated();
    }

    public Mono<Long> update(RelationalPath<?> table, Function<SQLUpdateClause, SQLUpdateClause> build) {
        return buildGenericExecuteSpec(build.apply(sqlQueryFactory.update(table)).getSQL())
                .fetch()
                .rowsUpdated();
    }

    public <T> Flux<T> insertWithGeneratedValues(
            RelationalPath<?> table,
            Function<SQLInsertClause, SQLInsertClause> build,
            FactoryExpressionBase<T> projection) {
        DatabaseClient.GenericExecuteSpec spec = buildGenericExecuteSpec(build.apply(sqlQueryFactory.insert(table)).getSQL());

        List<Expression<?>> febArgs = projection.getArgs();
        String[] columns = toColumnNameList(febArgs).toArray(new String[febArgs.size()]);

        return spec
                .filter(statement -> statement.returnGeneratedValues(columns))
                .map(row -> {
                    Object[] args = new Object[febArgs.size()];
                    int febIndex = 0;
                    for (Expression<?> febArg : febArgs) {
                        args[febIndex] = row.get(columns[febIndex], febArg.getType());
                        febIndex++;
                    }

                    return projection.newInstance(args);
                }).all();
    }

    public <T> Flux<? extends T> insertWithGeneratedValue(
            RelationalPath<?> table, Function<SQLInsertClause,
            SQLInsertClause> build,
            Path<T> property) {
        DatabaseClient.GenericExecuteSpec spec = buildGenericExecuteSpec(build.apply(sqlQueryFactory.insert(table)).getSQL());

        String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, property.getMetadata().getName());

        return spec
                .filter(statement -> statement.returnGeneratedValues(columnName))
                .map(row -> row.get(0, property.getType())).all();
    }


}
