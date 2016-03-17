package io.ganguo.opensdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 微信 分享内容
 * <p/>
 * Created by aaron on 10/21/15.
 */
public class WechatShare implements Parcelable, Serializable {

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
    private String titleUrl;

    /**
     * 分享图片网络地址 可选填参数 10KB以内
     */
    private String imageUrl;

    /**
     * 分享图片本地路径 可选填参数 10M以内
     */
    private String imagePath;

    /**
     * 分享文件路径
     */
    private String filePath;

    /**
     * 分享音乐链接
     */
    private String musicUrl;

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

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    @Override
    public String toString() {
        return "WechatShare{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", titleUrl='" + titleUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", filePath='" + filePath + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
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
        dest.writeString(this.titleUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imagePath);
        dest.writeString(this.filePath);
        dest.writeString(this.musicUrl);
    }

    public WechatShare() {
    }

    protected WechatShare(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.titleUrl = in.readString();
        this.imageUrl = in.readString();
        this.imagePath = in.readString();
        this.filePath = in.readString();
        this.musicUrl = in.readString();
    }

    public static final Creator<WechatShare> CREATOR = new Creator<WechatShare>() {
        public WechatShare createFromParcel(Parcel source) {
            return new WechatShare(source);
        }

        public WechatShare[] newArray(int size) {
            return new WechatShare[size];
        }
    };
}
