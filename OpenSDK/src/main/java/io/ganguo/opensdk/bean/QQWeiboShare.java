package io.ganguo.opensdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tony on 10/23/15.
 */
public class QQWeiboShare implements Parcelable, Serializable {

    /**
     * 分享标题 必填参数 512Bytes以内
     */
    private String title;

    /**
     * 分享内容 必填参数 1KB以内
     */
    private String content;

    /**
     * 标题链接 必填参数 10KB以内
     */
    private String url;

    /**
     * 分享图片网络地址 可选填参数 10KB以内
     */
    private String imageUrl;

    /**
     * 分享图片本地路径 可选填参数 10M以内
     */
    private String imagePath;

    /**
     * 纬度 -90.0到+90.0 +表示北纬
     */
    private float latitude;

    /**
     * 经度 -180.0到+180.0 +表示东经
     */
    private float longitude;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "QQWeiboShare{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imagePath);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
    }

    public QQWeiboShare() {
    }

    protected QQWeiboShare(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.imagePath = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
    }

    public static final Creator<QQWeiboShare> CREATOR = new Creator<QQWeiboShare>() {
        public QQWeiboShare createFromParcel(Parcel source) {
            return new QQWeiboShare(source);
        }

        public QQWeiboShare[] newArray(int size) {
            return new QQWeiboShare[size];
        }
    };
}
