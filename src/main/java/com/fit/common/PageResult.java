package com.fit.common;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    private Long total;
    private Long pageNum;
    private Long pageSize;
    private List<T> list;
    
    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setPageNum((long) pageInfo.getPageNum());
        result.setPageSize((long) pageInfo.getPageSize());
        result.setList(pageInfo.getList());
        return result;
    }
}
