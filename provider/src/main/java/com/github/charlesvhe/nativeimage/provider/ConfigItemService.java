package com.github.charlesvhe.nativeimage.provider;

import com.github.charlesvhe.nativeimage.PagingRequest;
import com.github.charlesvhe.nativeimage.PagingResponse;
import com.github.charlesvhe.nativeimage.QueryDslR2dbcTemplate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.github.charlesvhe.nativeimage.provider.QConfigItem.qConfigItem;

@RestController
@RequestMapping("/ConfigItem")
public class ConfigItemService {
    @Autowired
    protected QueryDslR2dbcTemplate queryDslR2DbcTemplate;
    // 解决GET方法传递filter对象问题
    public class Paging extends PagingRequest<ConfigItem> {
    }

    @PostMapping
    public Mono<ConfigItem> post(@RequestBody ConfigItem configItem) {
        return queryDslR2DbcTemplate.insertWithGeneratedValues(qConfigItem,
                sqlInsertClause -> sqlInsertClause.populate(configItem),
                Projections.bean(ConfigItem.class, qConfigItem.id, qConfigItem.gmtCreate)
        ).next();
    }

    @DeleteMapping("/{id:\\d+}")
    public Mono<Long> delete(@PathVariable Long id) {
        return queryDslR2DbcTemplate.delete(qConfigItem,
                sqlDeleteClause -> sqlDeleteClause.where(qConfigItem.id.eq(id)));
    }

    @PutMapping("/{id:\\d+}")
    public Mono<Long> put(@PathVariable Long id, @RequestBody ConfigItem configItem) {
        return queryDslR2DbcTemplate.update(qConfigItem,
                sqlUpdateClause -> sqlUpdateClause
                        .populate(configItem)
                        .where(qConfigItem.id.eq(id))
        );
    }

    @GetMapping("/{id:\\d+}")
    public Mono<ConfigItem> get(@PathVariable Long id) {
        return queryDslR2DbcTemplate.select(q -> q
                .select(qConfigItem)
                .from(qConfigItem)
                .where(qConfigItem.id.eq(id))
        ).next();
    }

    @GetMapping
    public Mono<PagingResponse<ConfigItem, ConfigItem>> get(ConfigItemService.Paging paging) {
        return queryDslR2DbcTemplate.select(q -> q
                        .select(Projections.bean(ConfigItem.class, QueryDslR2dbcTemplate.buildColumn(paging.getFields(), qConfigItem.getColumns())))
                        .from(qConfigItem)
                        .where(QueryDslR2dbcTemplate.buildWhere(paging.getFilter(), qConfigItem.getColumns(), ExampleMatcher.StringMatcher.CONTAINING))
                        .orderBy(QueryDslR2dbcTemplate.buildOrder(paging.getSort(), qConfigItem.getColumns()))
                        .offset(paging.getOffset())
                        .limit(paging.getLimit())
                )
                .collectList()
                .map(data -> new PagingResponse(data, paging.next()));
    }
}
