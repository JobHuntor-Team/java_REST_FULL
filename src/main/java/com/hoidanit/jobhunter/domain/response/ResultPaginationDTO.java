package com.hoidanit.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private MetaDTO meta;
    private Object result;

    @Getter
    @Setter
    public static class MetaDTO {
        private int Page; // số trang hiện tại
        private int PageSize; // số lượng Phần tử trên mỗi trang
        private int TotalPage; // tổng số trang
        private long TotalItem; // tổng số Phân tử
    }
}
