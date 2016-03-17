package io.ganguo.library.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import io.ganguo.library.R;

/**
 * View 处理工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Views {

    /**
     * 设置背景
     *
     * @param view
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    public static void setBackground(View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Set max text length for textview
     *
     * @param textView
     * @param maxLength
     */
    public static void setMaxLength(TextView textView, int maxLength) {
        if (textView == null) {
            return;
        }
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);
        textView.setFilters(filters);
    }

    /**
     * 当ScrollView与ListView冲突的时候，用这个方法设置listView的高
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 根据tag找到对应的view
     *
     * @param viewGroup
     * @param tag
     * @return
     */
    public static List<View> findViewByTag(ViewGroup viewGroup, Object tag) {
        List<View> views = new LinkedList<>();
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(findViewByTag((ViewGroup) child, tag));
            } else {
                if (Beans.isEquals(child.getTag(), tag)) {
                    views.add(child);
                }
            }
        }
        return views;
    }

    /**
     * 记住view显示状态
     *
     * @param view
     */
    public static void saveVisible(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                //还原所有view
                saveVisible(viewGroup.getChildAt(i));
            }
        } else {
            view.setTag(R.string.tag_view_visible, view.getVisibility());
        }
    }

    /**
     * 复原view的显示
     *
     * @param view
     */
    @SuppressWarnings("ResourceType")
    public static void restoreVisible(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                //还原所有view
                View child = viewGroup.getChildAt(i);
                Object visible = child.getTag(R.string.tag_view_visible);
                if (visible != null) {
                    child.setVisibility((int) visible);
                }
                if (child instanceof ViewGroup) {
                    restoreVisible(child);
                }
            }
        } else {
            Object visible = view.getTag(R.string.tag_view_visible);
            if (visible != null) {
                view.setVisibility((int) visible);
            }
        }
    }

    /**
     * 获取TextView, EditText等控件的文本内容
     *
     * @param tv
     * @return
     */
    public static String getText(TextView tv) {
        return tv.getText().toString();
    }


}
