package io.ganguo.library.ui.adapter;

import android.content.Context;

/**
 * ListView通用构建适配器
 * <p/>
 * Created by Tony on 4/2/15.
 */
public class CreatorAdapter<T> extends ListAdapter<T> {

    private IViewCreator<T> mCreator;

    public CreatorAdapter(Context context, IViewCreator<T> creator) {
        super(context);
        mCreator = creator;
    }

    @Override
    public ViewHolder createView(Context context, int position, T item) {
        return mCreator.createView(context, position, item);
    }

    @Override
    public void updateView(ViewHolder viewHolder, int position, T item) {
        mCreator.updateView(viewHolder, position, item);
    }
}
