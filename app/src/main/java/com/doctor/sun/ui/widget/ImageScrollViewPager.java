package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by heky on 17/6/5.
 */

public class ImageScrollViewPager extends ViewPager {
    Activity mActivity; // 上下文
    List<View> mListViews; // 图片组
    int mScrollTime = 0;
    Timer timer;
    int oldIndex = 0;
    int curIndex = 0;
    private MyPagerAdapter adapter = null;
    LinearLayout mOvalLayout = null;

    public ImageScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始广告滚动
     *
     * @param mainActivity     显示广告的主界面
     * @param imgList          图片列表, 不能为null ,最少一张
     * @param scrollTime       滚动间隔 ,0为不滚动
     * @param ovalLayout       圆点容器,可为空,LinearLayout类型
     * @param ovalLayoutId     ovalLayout为空时 写0, 圆点layout XMl
     * @param ovalLayoutItemId ovalLayout为空时 写0,圆点layout XMl 圆点XMl下View ID
     * @param focusedId        ovalLayout为空时 写0, 圆点layout XMl 选中时的动画
     * @param normalId         ovalLayout为空时 写0, 圆点layout XMl 正常时背景
     */
    public void start(Activity mainActivity, List<View> imgList, int scrollTime, LinearLayout ovalLayout,
                      int ovalLayoutId, int ovalLayoutItemId, int focusedId, int normalId) {
        mActivity = mainActivity;
//		KJLoger.debug("aaa:"+mListViews);
        mListViews = imgList;
        mScrollTime = scrollTime;
        adapter = new MyPagerAdapter();
        // 设置圆点
        setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId, normalId);
        this.setAdapter(adapter);// 设置适配器

        if (scrollTime != 0 && imgList.size() > 1) {
            // 设置滑动动画时间 ,如果用默认动画时间可不用 ,反射技术实现
            new FixedSpeedScroller(mActivity).setDuration(this, 700);

            startTimer();
            // 触摸时停止滚动
            this.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startTimer();
                    } else {
                        stopTimer();
                    }
                    return false;
                }
            });
        }
//		if (mListViews.size() > 1) {
//			this.setCurrentItem((Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2) % mListViews.size());// 设置选中为中间/图片为和第0张一样
//		}
        if (mListViews.size() > 1) {
            this.setCurrentItem(0);
        }
    }

    // 设置圆点
    private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId, final int ovalLayoutItemId,
                               final int focusedId, final int normalId) {
        if (ovalLayout != null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            for (int i = 0; i < mListViews.size(); i++) {
                ovalLayout.addView(inflater.inflate(ovalLayoutId, null));

            }
            // 选中第一个
            ovalLayout.getChildAt(0).findViewById(ovalLayoutItemId).setBackgroundResource(focusedId);
            this.setOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int i) {
                    curIndex = i % mListViews.size();
                    // 取消圆点选中
                    ovalLayout.getChildAt(oldIndex).findViewById(ovalLayoutItemId).setBackgroundResource(normalId);
                    // 圆点选中
                    ovalLayout.getChildAt(curIndex).findViewById(ovalLayoutItemId).setBackgroundResource(focusedId);
                    oldIndex = curIndex;
                }

                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                public void onPageScrollStateChanged(int arg0) {
                }
            });
            mOvalLayout = ovalLayout;
        }
    }

    /**
     * 取得当明选中下标
     *
     * @return
     */
    public int getCurIndex() {
        return curIndex;
    }

    public void stop() {
        stopTimer();
        if (mOvalLayout != null) {
//			mOvalLayout.removeAllViewsInLayout();
            mOvalLayout.removeAllViews();
        }
        this.removeAllViews();
        mListViews = null;
//		adapter = null;
        this.setAdapter(null);
        this.setAdapter(adapter);
    }

    /**
     * 停止滚动
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    /**
     * 开始滚动
     */
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageScrollViewPager.this.setCurrentItem(ImageScrollViewPager.this.getCurrentItem() + 1);
                    }
                });
            }
        }, mScrollTime, mScrollTime);
    }

    // 适配器 //循环设置
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
//		public int getItemPosition(Object object) {
//			// TODO Auto-generated method stub
//			return POSITION_NONE;
//		}

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mListViews == null) {
                return 0;
            }
            if (mListViews.size() == 1) {// 一张图片时不用流动
                return 1;
            }
            return Integer.MAX_VALUE;//设置成最大,使用户看不到边界
        }

        @Override
        public Object instantiateItem(View v, int i) {
            if (mListViews.get(i % mListViews.size()).getParent() == null) {

                ((ViewPager) v).addView(mListViews.get(i % mListViews.size()));
            } else {
                ((ViewPager) mListViews.get(i % mListViews.size()).getParent()).removeView(mListViews.get(i % mListViews.size()));
                ((ViewPager) v).addView(mListViews.get(i % mListViews.size()));
            }

//			if (((ViewPager) v).getChildCount() == mListViews.size()) {
//				((ViewPager) v).removeView(mListViews.get(i % mListViews.size()));
//			}
//			((ViewPager) v).addView(mListViews.get(i % mListViews.size()), 0);
            return mListViews.get(i % mListViews.size());
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
//			((ViewPager) arg0).removeView(mListViews.get(arg1));
//			 View view = (View)arg2;
//			 ((ViewPager) arg0).removeView(view);
//			 view = null;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
