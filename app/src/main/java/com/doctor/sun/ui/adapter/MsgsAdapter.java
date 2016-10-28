package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.ItemMsgsBinding;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.vo.LayoutId;

import io.ganguo.library.core.image.GGlide;
import io.ganguo.library.util.date.Date;

/**
 * Created by Lynn on 1/15/16.
 */
@Deprecated
public class MsgsAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Appointment appointment;

    public MsgsAdapter( Appointment appointment) {
        this.appointment = appointment;
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_msgs;
    }
}
