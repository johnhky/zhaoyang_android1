package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.ui.activity.HtmlBrowserActivity;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.activity.WebBrowserActivity;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 21/7/2016.
 */

public class BannerDetail {
    public static final int BANNER_TYPE_PICTURE = 1;
    public static final int BANNER_TYPE_HTML_STRING = 2;
    public static final int BANNER_TYPE_WEB_URL = 3;
    /**
     * content : http://7xkt51.com2.z0.glb.qiniucdn.com/FqNXYpUv-7DN0K6h2zYy2e2ZuJ83
     * header : 测试之
     * type : 1
     */
    @JsonProperty("content")
    private String content;
    @JsonProperty("header")
    private String header;
    @JsonProperty("type")
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void viewDetail(Context context) {
        switch (type) {
            case BANNER_TYPE_PICTURE: {
                Intent intent = ImagePreviewActivity.makeIntent(context, getContent(), header);
                context.startActivity(intent);
                break;
            }
            case BANNER_TYPE_HTML_STRING: {
                Intent intent = HtmlBrowserActivity.intentFor(context, getContent(), header);
                context.startActivity(intent);
                break;
            }
            case BANNER_TYPE_WEB_URL: {
                Intent intent = WebBrowserActivity.intentFor(context, getContent(), header);
                context.startActivity(intent);
                break;
            }
        }
    }
}
