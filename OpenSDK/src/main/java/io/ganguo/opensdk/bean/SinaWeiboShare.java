package io.ganguo.opensdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 新浪微博 分享内容
 * <p/>
 * Created by aaron on 10/21/15.
 */
public class SinaWeiboShare implements Parcelable, Serializable {

    /**
     * 分享内容 必填参数 不能超过140个汉字
     */
    private String content;

    /**
     * 分享网络图片地址 可选填参数
     * 图片最大5M 仅支持JPEG GIF、PNG格式
     * <p/>
     * 注意：需要申请高级写入接口，否则会分享失败
     */
    private String imageUrl;

    /**
     * 分享本地图片路径 可选填参数
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
        return "SinaWeiboShare{" +
                "content='" + content + '\'' +
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
        dest.writeString(this.content);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imagePath);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
    }

    public SinaWeiboShare() {
    }

    protected SinaWeiboShare(Parcel in) {
        this.content = in.readString();
        this.imageUrl = in.readString();
        this.imagePath = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
    }

    public static final Creator<SinaWeiboShare> CREATOR = new Creator<SinaWeiboShare>() {
        public SinaWeiboShare createFromParcel(Parcel source) {
            return new SinaWeiboShare(source);
        }

        public SinaWeiboShare[] newArray(int size) {
            return new SinaWeiboShare[size];
        }
    };
}