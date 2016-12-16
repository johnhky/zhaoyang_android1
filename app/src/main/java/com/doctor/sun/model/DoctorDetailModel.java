package com.doctor.sun.model;

import com.doctor.sun.R;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.ItemDescription;
import com.doctor.sun.vm.ItemTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kb on 13/12/2016.
 */

public class DoctorDetailModel {

    public List<SortedItem> parseData(Doctor data) {

        List<SortedItem> result = new ArrayList<>();

        ItemTitle introductionTitle = new ItemTitle(R.layout.item_title, "简介");
        introductionTitle.setItemId("introductionTitle");
        introductionTitle.setPosition(result.size());
        result.add(introductionTitle);

        ItemDescription introduction = new ItemDescription();
        introduction.setMainContent("我是钢铁侠");
        introduction.setItemId("introduction");
        introduction.setPosition(result.size());
        result.add(introduction);

        ModelUtils.insertSpace(result, R.layout.divider_13dp_gray);

        ItemTitle articleTitle = new ItemTitle(R.layout.item_title, "文章");
        articleTitle.setSubtitle("more");
        articleTitle.setItemId("articleTitle");
        articleTitle.setPosition(result.size());
        result.add(articleTitle);

        ItemDescription article = new ItemDescription();
        article.setMainContent("欢迎使用昭阳医生");
        article.setSubContent("2016-00-00 00:00:00");
        article.setItemId("article");
        article.setPosition(result.size());
        result.add(article);

        ModelUtils.insertSpace(result, R.layout.divider_13dp_gray);

        ItemTitle commentTitle = new ItemTitle(R.layout.item_title, "评论");
        commentTitle.setItemId("commentTitle");
        commentTitle.setPosition(result.size());
        result.add(commentTitle);

        ItemDescription comments = new ItemDescription();
        comments.setMainContent("我是美国队长");
        comments.setRatingPoint(Float.valueOf("3.6"));
        comments.setItemId("comments");
        comments.setPosition(result.size());
        result.add(comments);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        return result;
    }
}
