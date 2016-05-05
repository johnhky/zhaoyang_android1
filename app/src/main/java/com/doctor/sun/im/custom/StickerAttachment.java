package com.doctor.sun.im.custom;

import com.doctor.sun.util.JacksonUtils;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class StickerAttachment implements MsgAttachment {

    private final String KEY_CATALOG = "catalog";
    private final String KEY_CHARTLET = "chartlet";


    private String catalog;
    private String chartlet;

    public StickerAttachment() {
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getChartlet() {
        return chartlet;
    }

    public void setChartlet(String chartlet) {
        this.chartlet = chartlet;
    }

    @Override
    public String toJson(boolean b) {
        return JacksonUtils.toJson(this);
    }
}
