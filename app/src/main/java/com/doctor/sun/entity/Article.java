package com.doctor.sun.entity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.vm.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 1/4/2016.
 */
public class Article implements LayoutId {

    /**
     * title : 文章标题
     * url : http://www.baidu.com
     */

    @JsonProperty("title")
    private String title;
    @JsonProperty("url")
    private String url;
    @JsonProperty("id")
    private String id;
    @JsonProperty("image")
    private String image;
    @JsonProperty("content")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void openUrl(View view) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getUrl());
        intent.setData(content_url);
        view.getContext().startActivity(intent);
    }

    @Override
    public String toString() {
        return "data{id:" + id + ",image:" + image + ",content:" + content + ",title:" + title + "url:" + url + "}";
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_article;
    }
}
