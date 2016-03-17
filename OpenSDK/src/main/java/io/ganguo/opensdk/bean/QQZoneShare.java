package io.ganguo.opensdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * QQ空间分享
 * <p/>
 * Created by Tony on 10/23/15.
 */
public class QQZoneShare implements Parcelable, Serializable {

    /**
     * 分享标题 必填参数 最多200个字符
     */
    private String title;

    /**
     * 分享标题链接 必填参数
     */
    private String titleUrl;

    /**
     * 分享内容 必填参数 最多600个字符
     */
    private String content;

    /**
     * 网站名称 必填参数
     */
    private String siteName;

    /**
     * 网站链接 必填参数
     */
    private String siteUrl;

    /**
     * 分享网络图片地址 可选填参数
     */
    private String imageUrl;

    /**
     * 分享本地图片路径 可选填参数
     */
    private String imagePath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
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
        return "QQZoneShare{" +
                "title='" + title + '\'' +
                ", titleUrl='" + titleUrl + '\'' +
                ", content='" + content + '\'' +
                ", siteName='" + siteName + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
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
        dest.writeString(this.siteName);
        dest.writeString(this.siteUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imagePath);
    }

    public QQZoneShare() {
    }

    protected QQZoneShare(Parcel in) {
        this.title = in.readString();
        this.titleUrl = in.readString();
        this.content = in.readString();
        this.siteName = in.readString();
        this.siteUrl = in.readString();
        this.imageUrl = in.readString();
        this.imagePath = in.readString();
    }

    public static final Parcelable.Creator<QQZoneShare> CREATOR = new Parcelable.Creator<QQZoneShare>() {
        public QQZoneShare createFromParcel(Parcel source) {
            return new QQZoneShare(source);
        }

        public QQZoneShare[] newArray(int size) {
            return new QQZoneShare[size];
        }
    };
}
