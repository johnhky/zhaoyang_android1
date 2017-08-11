package com.doctor.sun.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * Created by heky on 17/7/13.
 */

public class WindowManagerUtils {

    private Context mContext;
    private DisplayMetrics metric;
    private WeakReference<Context> wr;

    public WindowManagerUtils(Activity activity)
    {
        wr = new WeakReference<Context>(activity);
        mContext = wr.get();
        metric = new DisplayMetrics();
        Activity act = (Activity)mContext;
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
    }
    public WindowManagerUtils(Context con)
    {
        wr = new WeakReference<Context>(con);
        mContext = wr.get();
        metric = new DisplayMetrics();
        WindowManager wm = (WindowManager)con.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
		/*Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size); */
    }


    /**
     * 获取屏幕宽度
     * @return 返回宽度
     */
    public int getScreenWidth()
    {
        return metric.widthPixels;     // 屏幕宽度（像素）
    }

    /**
     * 获取屏幕高度
     * @return 返回高度
     */
    public int getScreenHeight()
    {
        return metric.heightPixels;   // 屏幕高度（像素）
    }


    /**
     *
     * @param child
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


}
