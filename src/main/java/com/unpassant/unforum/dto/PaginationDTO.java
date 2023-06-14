package com.unpassant.unforum.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showLastPage;
    private Integer page; //当前页
    private List<Integer> pages = new ArrayList<>(); //当前页码的左右页码
    private Integer totalPage;//当前最大页码数


    //手敲分页逻辑功能
    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;

        //设置当前页码值等于传过来的值
        this.page=page;
        //填充pages
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            //左
            if (page - 1 > 0 && page - i >0) {
                pages.add(0,page - i);
            }
            //右
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //是否展示上一页
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }

        //是否展示下一页
        if(page == totalPage){
            showNext = false;

        }else{
            showNext = true;
        }

        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage = false;
        }else{
            showFirstPage = true;
        }

        //是否展示最后一页
        if(pages.contains(totalPage)){
            showLastPage = false;
        }else{
            showLastPage = true;
        }
    }
}
