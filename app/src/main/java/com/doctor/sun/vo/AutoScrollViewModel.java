package com.doctor.sun.vo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.doctor.sun.R;
import com.doctor.sun.ui.pager.BindingPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.ui.widget.AutoScrollViewPager;


/**
 * Created by rick on 10/14/15.
 */
public class AutoScrollViewModel {

    private List<LayoutId> bannerImages = new ArrayList<>();
    private BindingPagerAdapter<LayoutId> stringImagePagerAdapter = new BindingPagerAdapter<>();

    public List<LayoutId> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(List<? extends LayoutId> bannerImages) {
        this.bannerImages.addAll(bannerImages);
    }

    public PagerAdapter getPagerAdapter() {
        BindingPagerAdapter mAdapter = stringImagePagerAdapter;
        mAdapter.setItems(bannerImages);
        return mAdapter;
    }


    public boolean autoScroll(AutoScrollViewPager vp, Object llyBannerDots, boolean autoScroll) {
        if (llyBannerDots != null) {
            int size = vp.getAdapter().getCount();
            AutoScrollViewModel.RefreshIndicatorListener listener = new AutoScrollViewModel.RefreshIndicatorListener(size, (LinearLayout) llyBannerDots);
            listener.refreshDotViews(0, size);
            vp.addOnPageChangeListener(listener);
        }
        if (autoScroll) {
            vp.startAutoScroll();
        }
        return !bannerImages.isEmpty();
    }


    public static class RefreshIndicatorListener implements ViewPager.OnPageChangeListener {
        public static final int DOT_SIZE = 14;
        private int itemCount;
        private LinearLayout mDotViews;
        private int mCurrentDotPos = 0;

        public RefreshIndicatorListener(int itemCount, LinearLayout mDotViews) {
            this.mDotViews = mDotViews;
            this.itemCount = itemCount;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            refreshDotViews(position, itemCount);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        /**
         * 显示banner点
         *
         * @param pos
         * @param size
         */
        public void refreshDotViews(int pos, int size) {
            mCurrentDotPos = pos;
            mDotViews.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    DOT_SIZE, DOT_SIZE);
            layoutParams.setMargins(8, 15, 8, 15);
            for (int i = 0; i < size; i++) {
                ImageView iv = new ImageView(mDotViews.getContext());
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.drawable.bg_transparent);
                iv.setClickable(false);
                if (i == pos) {
                    iv.setImageResource(R.drawable.bg_transparent);
                }
                mDotViews.addView(iv);
            }
        }
    }
}
