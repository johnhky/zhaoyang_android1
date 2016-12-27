package com.doctor.sun.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.fragment.DoctorArticleFragment;
import com.doctor.sun.vm.ItemDescription;
import com.doctor.sun.vm.ItemTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kb on 13/12/2016.
 */

public class DoctorDetailModel {

    public List<SortedItem> parseData(final Doctor data, List<Article> articleList, List<Comment> commentList) {

        List<SortedItem> result = new ArrayList<>();

        ModelUtils.insertSpace(result, R.layout.divider_8dp_gray);

        ItemTitle introductionTitle = new ItemTitle(R.layout.item_title, "简介");
        introductionTitle.setItemId("introductionTitle");
        introductionTitle.setPosition(result.size());
        result.add(introductionTitle);

        String detail;
        ItemDescription introduction = new ItemDescription();
        if (data.getDetail() != null && !data.getDetail().equals("")) {
            detail = data.getDetail();
        } else {
            detail = "暂时未有医生相关简介";
            introduction.setEnabled(false);
        }
        introduction.setMainContent(detail);
        introduction.setItemId("introduction");
        introduction.setPosition(result.size());
        result.add(introduction);

        ModelUtils.insertSpace(result, R.layout.divider_8dp_gray);

        ItemTitle articleTitle = new ItemTitle(R.layout.item_title, "文章");
        articleTitle.setSubtitle("more");
        articleTitle.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = DoctorArticleFragment.getArgs(data.getId());
                Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "文章", bundle);
                v.getContext().startActivity(intent);
            }
        });
        articleTitle.setItemId("articleTitle");
        articleTitle.setPosition(result.size());
        result.add(articleTitle);

        if (articleList.size() > 0) {
            Article articleDetail = articleList.get(0);
            ItemDescription article = new ItemDescription();
            article.setMainContent(articleDetail.getTitle());
            article.setItemId("article");
            article.setPosition(result.size());
            result.add(article);
        } else {
            ItemDescription noArticle = new ItemDescription();
            noArticle.setEnabled(false);
            noArticle.setMainContent("暂时未有医生相关文章");
            noArticle.setItemId("noArticle");
            noArticle.setPosition(result.size());
            result.add(noArticle);
        }


        ModelUtils.insertSpace(result, R.layout.divider_8dp_gray);

        ItemTitle commentTitle = new ItemTitle(R.layout.item_title, "评论");
        commentTitle.setItemId("commentTitle");
        commentTitle.setPosition(result.size());
        result.add(commentTitle);

        if (commentList.size() > 0) {
            for (int i = 0; i < commentList.size() && i < 3; i++) {
                Comment e = commentList.get(i);
                e.setPosition(result.size());
                result.add(e);
            }
        } else {
            ItemDescription noComment = new ItemDescription();
            noComment.setEnabled(false);
            noComment.setMainContent("暂时未有该医生的相关评论");
            noComment.setItemId("noComment");
            noComment.setPosition(result.size());
            result.add(noComment);
        }

        ModelUtils.insertSpace(result, R.layout.space_370dp_gray);

        return result;
    }
}
