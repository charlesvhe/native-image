package com.github.charlesvhe.nativeimage;

import lombok.Data;

@Data
public class PagingRequest<F> {
    private F filter;
    // 使用offset、limit分页兼容游标、地理坐标查询
    private long offset = 0;
    private int limit = 10;
    private long total = -1;
    private String sort;
    private String fields;

    public boolean needCount() {
        // total负数则需要从数据库count取最新值
        return total < 0;
    }

    public PagingRequest<F> next(long total) {
        PagingRequest<F> next = new PagingRequest<F>();
        next.filter = this.filter;
        next.offset = this.offset + this.limit;
        next.limit = this.limit;
        next.total = total;
        next.sort = this.sort;
        next.fields = this.fields;
        return next;
    }

    public PagingRequest<F> next() {
        return next(this.total);
    }

}
