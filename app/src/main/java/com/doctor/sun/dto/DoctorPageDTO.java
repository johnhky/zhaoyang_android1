package com.doctor.sun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 11/5/15.
 */
public class DoctorPageDTO<T> {


    /**
     * total : 1
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * next_page_url :
     * prev_page_url :
     * from : 1
     * to : 1
     * data : []
     */

    @JsonProperty("total")
    private String total;
    @JsonProperty("per_page")
    private String perPage;
    @JsonProperty("current_page")
    private String currentPage;
    @JsonProperty("last_page")
    private String lastPage;
    @JsonProperty("next_page_url")
    private String nextPageUrl;
    @JsonProperty("prev_page_url")
    private String prevPageUrl;
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("list")
    private List<T> list;

    public void setTotal(String total) {
        this.total = total;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(List<T> list) {
        this.list = list;
    }

    public String getTotal() {
        return total;
    }

    public String getPerPage() {
        return perPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<T> getList() {
        return list;
    }


    @Override
    public String toString() {
        return "PageDTO{" +
                "total='" + total + '\'' +
                ", perPage='" + perPage + '\'' +
                ", currentPage='" + currentPage + '\'' +
                ", lastPage='" + lastPage + '\'' +
                ", nextPageUrl='" + nextPageUrl + '\'' +
                ", prevPageUrl='" + prevPageUrl + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", data=" + list +
                '}';
    }
}
