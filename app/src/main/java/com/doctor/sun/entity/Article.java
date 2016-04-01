package com.doctor.sun.entity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
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

    public void openUrl(View view) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getUrl());
        intent.setData(content_url);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_article;
    }
}
