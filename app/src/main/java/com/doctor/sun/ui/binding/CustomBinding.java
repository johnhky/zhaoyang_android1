package com.doctor.sun.ui.binding;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doctor.sun.util.FragmentFactory;


/**
 */
public class CustomBinding {

    @android.databinding.BindingAdapter(value = {"android:src"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @android.databinding.BindingAdapter(value = {"android:src"})
    public static void loadImage(ImageView imageView, int resourceId) {
        Glide.with(imageView.getContext()).load(resourceId).into(imageView);
    }

    @android.databinding.BindingAdapter(value = {"android:src", "app:width", "app:height"})
    public static void loadImage(ImageView imageView, String url, int width, int height) {
        Glide.with(imageView.getContext()).load(url).override(width, height).into(imageView);
    }

    @android.databinding.BindingAdapter(value = {"android:src", "android:drawable"})
    public static void loadImage(ImageView imageView, String url, Drawable icon) {
        Glide.with(imageView.getContext()).load(url).placeholder(icon).into(imageView);
    }

    @android.databinding.BindingAdapter(value = {"android:src", "android:drawable"})
    public static void loadAvatar(ImageView imageView, String url, @DrawableRes int icon) {
        Glide.with(imageView.getContext()).load(url).placeholder(icon).into(imageView);
    }


    //    @android.databinding.BindingAdapter(value = {"android:src"})
//    public static void loadImage(ImageView imageView, int resource) {
//        Glide.with(imageView.getContext()).load(resource).into(imageView);
//    }
    @BindingAdapter(value = "app:srcCompat")
    public static void srcCompat(ImageView imageView, int resourceId) {
        Drawable d = VectorDrawableCompat.create(imageView.getResources(), resourceId, null);
        Drawable dWrap = DrawableCompat.wrap(d);
        imageView.setImageDrawable(dWrap);
    }


    @BindingAdapter(value = "app:backgroundCompat")
    public static void backgroundCompat(View view, @DrawableRes int background) {
        Drawable d = VectorDrawableCompat.create(view.getResources(), background, null);
        Drawable dWrap = DrawableCompat.wrap(d);
        if (dWrap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(dWrap);
            } else {
                view.setBackgroundDrawable(dWrap);
            }
        }
    }

    @android.databinding.BindingAdapter("app:drawableRightCompat")
    public static void drawableRightCompat(TextView view, @DrawableRes int id) {
        Drawable d = VectorDrawableCompat.create(view.getResources(), id, null);
        Drawable dWrap = DrawableCompat.wrap(d);
        if (dWrap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
            }
        }
    }


    @android.databinding.BindingAdapter("app:drawableTopCompat")
    public static void drawableTopCompat(TextView view, @DrawableRes int id) {
        Drawable d = VectorDrawableCompat.create(view.getResources(), id, null);
        Drawable dWrap = DrawableCompat.wrap(d);
        if (dWrap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
            }
        }
    }

    @BindingAdapter(value = "app:drawableTop")
    public static void drawableTop(TextView textView, @DrawableRes int top) {
        setCompoundVectorDrawables(textView, 0, top, 0, 0);
    }

    /**
     * AppCompat 23.3.0 removed support of vector drawables attributes like
     * drawableLeft, from xml.  Use this method to use a vector drawable as
     * a compound drawable of a TextView.
     */
    public static void setCompoundVectorDrawables(TextView textView,
                                                  @DrawableRes int start,
                                                  @DrawableRes int top,
                                                  @DrawableRes int end,
                                                  @DrawableRes int bottom) {
        // On Marshmallow, if we use the setCompoundDrawables* methods which take
        // VectorDrawableCompat arguments, the rendering is incorrect (the text
        // appears on top of the icon).  If we use the setCompoundDrawable* methods
        // which take resource ids instead, we don't have this problem. Since the
        // method which takes resource ids is available on lollipop, we use
        // it starting from lollipop.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setCompoundVectorDrawablesV21(textView, start, top, end, bottom);
        } else {
            Resources resources = textView.getResources();
            android.support.v4.widget.TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    textView,
                    createVectorDrawable(resources, start),
                    createVectorDrawable(resources, top),
                    createVectorDrawable(resources, end),
                    createVectorDrawable(resources, bottom));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setCompoundVectorDrawablesV21(TextView textView,
                                                      @DrawableRes int start,
                                                      @DrawableRes int top,
                                                      @DrawableRes int end,
                                                      @DrawableRes int bottom) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    private static VectorDrawableCompat createVectorDrawable(Resources resources, @DrawableRes int res) {
        if (res == 0) return null;
        return VectorDrawableCompat.create(resources, res, null);
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

    @android.databinding.BindingAdapter("app:html")
    public static void fromHtml(TextView view, String text) {
        if (text == null) return;
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

    @android.databinding.BindingAdapter("android:visibility2")
    public static void visibility2(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
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


    @BindingAdapter("android:fragment")
    public static void fragment(FrameLayout layout, FragmentFactory.FactoryCommand command) {
    }

}
