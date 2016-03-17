package io.ganguo.library.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * 使用这个，需要保证最少有3个view
 * 如果1个时不能滑动
 * 如果2个时，可以再添加多一次，到四个
 * <p/>
 * Created by Tony on 4/9/15.
 */
public class LoopPageAdapter extends CommonPageAdapter {

    public LoopPageAdapter() {
        super();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getItem(position);
        try {
            container.addView(view);
        } catch (Exception e) {
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(views.getList(position % views.size()));
    }

    @Override
    public int getCount() {
        if (getRealCount() < 3) {
            return getRealCount();
        }
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return getList() == null ? 0 : getList().size();
    }

    public int toRealPosition(int position) {
        return position % getRealCount();
    }

    @Override
    public View getItem(int position) {
        return super.getItem(toRealPosition(position));
    }

    public int getStartPosition() {
        return getRealCount() * 1000 % getRealCount();
    }
}
