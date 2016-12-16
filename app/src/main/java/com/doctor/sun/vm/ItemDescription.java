package com.doctor.sun.vm;

import com.doctor.sun.R;

/**
 * Created by kb on 13/12/2016.
 */

public class ItemDescription extends BaseItem {

    private String image;
    private String imageText;
    private String mainContent;
    private String subContent;
    private float ratingPoint;
    public boolean hasImage = false;
    public boolean hasImageText = false;
    public boolean hasSubContent = false;
    public boolean hasRating = false;

    public int getItemLayoutId() {
        return R.layout.item_description2;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        hasImage = true;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
        hasImageText = true;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
        hasSubContent = true;
    }

    public float getRatingPoint() {
        return ratingPoint;
    }

    public void setRatingPoint(float ratingPoint) {
        this.ratingPoint = ratingPoint;
        hasRating = true;
    }
}
