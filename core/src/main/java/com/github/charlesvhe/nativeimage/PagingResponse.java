package com.github.charlesvhe.nativeimage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse<D, F> {
    private D data;
    private PagingRequest<F> next;
}
