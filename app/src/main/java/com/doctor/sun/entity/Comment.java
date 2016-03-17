package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Lynn on 2/15/16.
 */
public class Comment implements  LayoutId {
    /**
     * "comment": "cdfef",
     * "patient_name": "Albee",
     * "avatar": "http://7xkt51.com2.z0.glb.qiniucdn.com/Fhp9vxDD8BY7-CNjXIhaB7YtJqS-",
     * "comment_time": "2015-11-11"
     **/

    @JsonProperty("comment")
    private String comment;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("comment_time")
    private String commentTime;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_comment;
    }
}
