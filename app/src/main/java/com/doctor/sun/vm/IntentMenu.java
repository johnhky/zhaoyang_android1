package com.doctor.sun.vm;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.doctor.sun.bean.Constants;


/**
 * Created by rick on 11/3/2016.
 */
public class IntentMenu extends BaseMenu {
    private int requestCode = -1;
    private final Class<?> clazz;
    private Parcelable parcelable;

    public IntentMenu(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title, Class<?> clazz) {
        super(itemLayoutId, icon, title);
        this.clazz = clazz;
    }


    public void setParcelable(Parcelable parcelable) {
        this.parcelable = parcelable;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void navigate(View v) {
        if (clazz == null) return;
        Intent i = new Intent();
        Activity context = (Activity) v.getContext();
        i.setClass(context, clazz);
        if (parcelable != null) {
            i.putExtra(Constants.DATA, parcelable);
        }
        if (requestCode > 0) {
            context.startActivityForResult(i, requestCode);
        } else {
            context.startActivity(i);
        }
    }

    @Override
    public View.OnClickListener itemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(v);
            }
        };
    }
}
