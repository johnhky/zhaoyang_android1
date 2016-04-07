package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.emoji.EmojiManager;
import com.doctor.sun.emoji.StickerManager;
import com.doctor.sun.ui.fragment.EmoticonFragment;
import com.doctor.sun.ui.fragment.StickerFragment;

/**
 * Created by rick on 7/4/2016.
 */
public class StickerPagerAdapter extends FragmentPagerAdapter {

    public StickerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        if (position < EmojiManager.getPageCount()) {
            return EmoticonFragment.newInstance(position);
        } else {
            return StickerFragment.newInstance(position);
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return EmojiManager.getPageCount() + StickerManager.getInstance().getTotalPage();
    }
}
