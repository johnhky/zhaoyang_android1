package com.doctor.sun.ui.binding;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 */
public class CustomBinding {
    @android.databinding.BindingAdapter(value = {"android:src", "app:placeHolder"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, Drawable icon) {
        Glide.with(imageView.getContext()).load(url).placeholder(icon).into(imageView);
    }

    @android.databinding.BindingAdapter("bind:onClick")
    public static void onClick(View view, View.OnClickListener listener) {
        if (null != listener) {
            view.setOnClickListener(listener);
        } else {
            view.setBackgroundColor(0x00000000);
        }
    }

    @android.databinding.BindingAdapter("bind:selected")
    public static void selected(View view, int id) {
        view.setSelected(view.getId() == id);
    }


    @android.databinding.BindingAdapter("bind:drawableLeft")
    public static void drawableLeft(TextView view, @DrawableRes int id) {
        view.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0);

    }

    @android.databinding.BindingAdapter("bind:drawableRight")
    public static void drawableRight(TextView view, @DrawableRes int id) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0);
    }

    @android.databinding.BindingAdapter("bind:html")
    public static void fromHtml(TextView view, String text) {
        view.setText(Html.fromHtml(text));
    }

    @android.databinding.BindingAdapter("bind:text")
    public static void fromHtmlToText(TextView view, String text) {
        view.setText(Html.fromHtml(text).toString());

    }

    @android.databinding.BindingAdapter("android:background")
    public static void background(View view, int color) {
        view.setBackgroundResource(color);
    }

    @android.databinding.BindingAdapter("bind:visibility")
    public static void visibility(View view, int visibility) {
        switch (visibility) {
            case 0:
                view.setVisibility(View.VISIBLE);
                break;
            case 4:
                view.setVisibility(View.INVISIBLE);
                break;
            case 8:
                view.setVisibility(View.GONE);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }

    @android.databinding.BindingAdapter("bind:visible")
    public static void visible(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
