package com.doctor.sun.im.custom;

/**
 * Created by rick on 11/4/2016.
 */
public class AttachmentData {
    private int type = -1;
    private String data;
    private String msg;
    private String extension;
    private int imageHeight;
    private int imageWidth;
    private long duration;
    private boolean shouldSkip = false;

    public String getBody() {
        return msg;
    }

    public void setBody(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isShouldSkip() {
        return shouldSkip;
    }

    public void setShouldSkip(boolean shouldSkip) {
        this.shouldSkip = shouldSkip;
    }
}
