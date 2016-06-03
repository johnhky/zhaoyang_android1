package com.doctor.sun.ui.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 */
public class CustomBinding {
    @android.databinding.BindingAdapter(value = {"android:src", "android:drawable"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, Drawable icon) {
        Glide.with(imageView.getContext()).load(url).placeholder(icon).into(imageView);
    }

    @android.databinding.BindingAdapter(value = {"android:src", "android:drawable"}, requireAll = false)
    public static void loadAvatar(ImageView imageView, String url, @DrawableRes int icon) {
        Glide.with(imageView.getContext()).load(url).placeholder(icon).into(imageView);
    }


    @android.databinding.BindingAdapter(value = {"android:src"}, requireAll = false)
    public static void loadImage(ImageView imageView, int resourceId) {
        Glide.with(imageView.getContext()).load(resourceId).into(imageView);
    }

    @android.databinding.BindingAdapter(value = {"android:src", "app:width", "app:height"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, int width, int height) {
        Glide.with(imageView.getContext()).load(url).override(width, height).into(imageView);
    }
//    @android.databinding.BindingAdapter(value = {"android:src"})
//    public static void loadImage(ImageView imageView, int resource) {
//        Glide.with(imageView.getContext()).load(resource).into(imageView);
//    }

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

    @android.databinding.BindingAdapter("android:visibility")
    public static void visibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @android.databinding.BindingAdapter("android:paddingLeft")
    public static void paddingLeft(View view, int padding) {
        view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    @android.databinding.BindingAdapter("android:paddingRight")
    public static void paddingRight(View view, int padding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
    }

    @android.databinding.BindingAdapter("bind:visible")
    public static void visible(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("android:selectDrawable")
    public static void selectDrawable(View view, int position) {
        if (view.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) view.getBackground();
            animation.stop();
            animation.selectDrawable(position);
        }
    }

}
