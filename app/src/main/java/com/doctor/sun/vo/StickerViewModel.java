package com.doctor.sun.vo;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.doctor.sun.R;
import com.doctor.sun.emoji.EmojiManager;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.pager.StickerPagerAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by rick on 7/4/2016.
 */
public class StickerViewModel {

    public final WeakReference<FragmentActivity> activity;

    private int pageCount = 0;
    private int currentPage = 0;

    public StickerViewModel(FragmentActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public FragmentPagerAdapter stickerAdapter() {
        return new StickerPagerAdapter(activity.get().getSupportFragmentManager());
    }

    public SimpleAdapter tabsAdapter() {

        SimpleAdapter simpleAdapter = new SimpleAdapter(activity.get());

        simpleAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_icon, "", null));
        simpleAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_ajmd, "", null));
        simpleAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_it, "", null));
        simpleAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_xxy, "", null));

        simpleAdapter.onFinishLoadMore(true);

        return simpleAdapter;
    }

    public ViewPager.OnPageChangeListener pageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }
}
