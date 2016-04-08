package com.doctor.sun.vo;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.IncludeStickerBinding;
import com.doctor.sun.emoji.EmojiManager;
import com.doctor.sun.emoji.StickerManager;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.SingleSelectAdapter;
import com.doctor.sun.ui.adapter.VpIndicatorAdapter;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.pager.StickerPagerAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by rick on 7/4/2016.
 */
public class StickerViewModel {

    public final WeakReference<FragmentActivity> activity;
    private final IncludeStickerBinding binding;
    private VpIndicatorAdapter vpIndicatorAdapter;
    private SingleSelectAdapter categoryAdapter;

    public StickerViewModel(FragmentActivity activity, IncludeStickerBinding binding) {
        this.activity = new WeakReference<>(activity);
        this.binding = binding;
    }

    public FragmentPagerAdapter stickerAdapter() {
        StickerPagerAdapter stickerPagerAdapter = new StickerPagerAdapter(activity.get().getSupportFragmentManager());
        return stickerPagerAdapter;
    }

    public VpIndicatorAdapter indicatorAdapter() {
        if (vpIndicatorAdapter == null) {
            vpIndicatorAdapter = new VpIndicatorAdapter(activity.get());
        }
        return vpIndicatorAdapter;
    }

    public SimpleAdapter tabsAdapter() {

        if (categoryAdapter == null) {
            categoryAdapter = new SingleSelectAdapter(activity.get(), onTabSelectedListener(), 0);

            categoryAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_icon, "", null));
            categoryAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_ajmd, "", null));
            categoryAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_it, "", null));
            categoryAdapter.add(new ClickMenu(R.layout.item_emoji_category, R.drawable.nim_emoji_xxy, "", null));

            categoryAdapter.onFinishLoadMore(true);
        }

        return categoryAdapter;
    }

    @NonNull
    private SingleSelectAdapter.OnSelectionChange onTabSelectedListener() {
        return new SingleSelectAdapter.OnSelectionChange() {
            @Override
            public void onSelectionChange(BaseAdapter adapter, int newSelectItem) {
                if (newSelectItem == 0) {
                    binding.scrPlugin.setCurrentItem(0);
                    return;
                }
                if (newSelectItem == 1) {
                    binding.scrPlugin.setCurrentItem(EmojiManager.getPageCount());
                    return;
                }
                if (newSelectItem == 2) {
                    binding.scrPlugin.setCurrentItem(EmojiManager.getPageCount() + getPagesBefore(0));
                    return;
                }
                if (newSelectItem == 3) {
                    binding.scrPlugin.setCurrentItem(EmojiManager.getPageCount() + getPagesBefore(1));
                    return;
                }

            }
        };
    }


    public ViewPager.OnPageChangeListener pageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int pageCount;
                int selectedPosition;

                int emoticonPageCount = EmojiManager.getPageCount();
                if (position < emoticonPageCount) {
                    pageCount = emoticonPageCount;
                    selectedPosition = position;
                    categoryAdapter.selectNoCallback(0);
                } else if (isTypeOf(0, position)) {
                    pageCount = getPageCount(0);
                    selectedPosition = position - emoticonPageCount;
                    categoryAdapter.selectNoCallback(1);
                } else if (isTypeOf(1, position)) {
                    pageCount = getPageCount(1);
                    selectedPosition = position - getPagesBefore(0) - emoticonPageCount;
                    categoryAdapter.selectNoCallback(2);
                } else {
                    pageCount = getPageCount(2);
                    selectedPosition = position - getPagesBefore(1) - emoticonPageCount;
                    categoryAdapter.selectNoCallback(3);
                }
                indicatorAdapter().setItemCount(pageCount);
                indicatorAdapter().setSelectedPosition(selectedPosition);
                indicatorAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private boolean isTypeOf(int type, int position) {
        return position < getPagesBefore(type) + EmojiManager.getPageCount();
    }

    private int getPageCount(int type) {
        return StickerManager.getInstance().getPageCount(type);
    }

    private int getPagesBefore(int type) {
        return StickerManager.getInstance().getPagesBefore(type);
    }
}
