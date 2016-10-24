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
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import java.util.List;

import io.ganguo.library.core.image.GGlide;
import io.ganguo.library.util.date.Date;

/**
 * Created by Lynn on 1/15/16.
 */
public class MsgsAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Context mActivity;
    private Appointment appointment;

    public MsgsAdapter(Context context, Appointment appointment) {
        super(context);
        mActivity = context;
        this.appointment = appointment;
    }

    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, int position) {
        if (vh.getItemViewType() == R.layout.item_msgs) {
            final ItemMsgsBinding binding = (ItemMsgsBinding) vh.getBinding();
            binding.setData((TextMsg) get(position));

            binding.tvTime.setText(new Date(((TextMsg) get(position)).getTime()).toFriendly(true));

            System.out.println(get(position));

            String avatar;
            String name;
            if (((TextMsg) get(position)).getDirection() == TextMsgFactory.DIRECTION_SEND) {
                avatar = Settings.getDoctorProfile().getAvatar();
                name = Settings.getDoctorProfile().getName();
            } else {
                //receive
                avatar = appointment.getAvatar();
                name = appointment.getPatientName();
            }
            System.out.println(avatar + name);
            binding.tvName.setText(name);

            GGlide.getGlide()
                    .with(mActivity)
                    .load(avatar)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(mActivity.getResources().getDrawable(R.drawable.default_avatar))
                    .into(new BitmapImageViewTarget(binding.ivAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmap =
                                    RoundedBitmapDrawableFactory.create(mActivity.getResources(), resource);
                            circularBitmap.setCircular(true);
                            binding.ivAvatar.setImageDrawable(circularBitmap);
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_msgs;
    }
}
