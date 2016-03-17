package io.ganguo.opensdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * QQ 分享内容
 * <p/>
 * Created by aaron on 10/21/15.
 */
public class QQShare implements Parcelable, Serializable {
    /**
     * 分享标题 必填参数 最多30个字符
     */
    private String title;

    /**
     * 标题链接 必填参数
     */
    private String titleUrl;

    /**
     * 分享内容 必填参数 最多40个字符
     */
    private String content;

    /**
     * 分享网络图片地址 可选填参数
     */
    private String imageUrl;

    /**
     * 分享本地图片路径 可选填参数
     */
    private String imagePath;

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "QQShareInfo{" +
                "title='" + title + '\'' +
                ", titleUrl='" + titleUrl + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.titleUrl);
        dest.writeString(this.content);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imagePath);
    }

    public QQShare() {
    }

    protected QQShare(Parcel in) {
        this.title = in.readString();
        this.titleUrl = in.readString();
        this.content = in.readString();
        this.imageUrl = in.readString();
        this.imagePath = in.readString();
    }

    public static final Creator<QQShare> CREATOR = new Creator<QQShare>() {
        public QQShare createFromParcel(Parcel source) {
            return new QQShare(source);
        }

        public QQShare[] newArray(int size) {
            return new QQShare[size];
        }
    };
}
