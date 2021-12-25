package com.whisht.heatapp.entity.http.resp;

import com.whisht.heatapp.entity.base.BaseResp;

import java.io.Serializable;
import java.util.List;

public class BasePageResp<T extends Serializable> extends BaseResp {
    private int pageIndex;	    //当前页	Int
    private int pageSize;	    //每页条数	Int
    private int maxPageSize;	//总页数	Int
    private int maxSize;	    //总条数	Int
    private List<T> dataList;    //管理指令列表

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
