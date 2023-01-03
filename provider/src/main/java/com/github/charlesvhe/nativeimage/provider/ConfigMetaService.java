package com.github.charlesvhe.nativeimage.provider;

import com.github.charlesvhe.nativeimage.PagingRequest;
import com.github.charlesvhe.nativeimage.PagingResponse;
import com.github.charlesvhe.nativeimage.QueryDslR2dbcTemplate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.github.charlesvhe.nativeimage.provider.QConfigMeta.qConfigMeta;

@RestController
@RequestMapping("/ConfigMeta")
public class ConfigMetaService {
    @Autowired
    protected QueryDslR2dbcTemplate queryDslR2DbcTemplate;

    // 解决GET方法传递filter对象问题
    public class Paging extends PagingRequest<ConfigMeta> {
    }

    @PostMapping
    public Mono<ConfigMeta> post(@RequestBody ConfigMeta configMeta) {
        return queryDslR2DbcTemplate.insertWithGeneratedValues(qConfigMeta,
                sqlInsertClause -> sqlInsertClause.populate(configMeta),
                Projections.bean(ConfigMeta.class, qConfigMeta.id, qConfigMeta.gmtCreate)
        ).next();
    }

    @DeleteMapping("/{id:\\d+}")
    public Mono<Long> delete(@PathVariable Long id) {
        return queryDslR2DbcTemplate.delete(qConfigMeta,
                sqlDeleteClause -> sqlDeleteClause.where(qConfigMeta.id.eq(id)));
    }

    @PutMapping("/{id:\\d+}")
    public Mono<Long> put(@PathVariable Long id, @RequestBody ConfigMeta configMeta) {
        return queryDslR2DbcTemplate.update(qConfigMeta,
                sqlUpdateClause -> sqlUpdateClause
                        .populate(configMeta)
                        .where(qConfigMeta.id.eq(id))
        );
    }

    @GetMapping("/{id:\\d+}")
    public Mono<ConfigMeta> get(@PathVariable Long id) {
        return queryDslR2DbcTemplate.select(q -> q
                .select(qConfigMeta)
                .from(qConfigMeta)
                .where(qConfigMeta.id.eq(id))
        ).next();
    }

    @GetMapping
    public Mono<PagingResponse<ConfigMeta, ConfigMeta>> get(Paging paging) {
        return queryDslR2DbcTemplate.select(q -> q
                        .select(Projections.bean(ConfigMeta.class, QueryDslR2dbcTemplate.buildColumn(paging.getFields(), qConfigMeta.getColumns())))
                        .from(qConfigMeta)
                        .where(QueryDslR2dbcTemplate.buildWhere(paging.getFilter(), qConfigMeta.getColumns(), ExampleMatcher.StringMatcher.CONTAINING))
                        .orderBy(QueryDslR2dbcTemplate.buildOrder(paging.getSort(), qConfigMeta.getColumns()))
                        .offset(paging.getOffset())
                        .limit(paging.getLimit())
                )
                .collectList()
                .map(data -> new PagingResponse(data, paging.next()));
    }
}
