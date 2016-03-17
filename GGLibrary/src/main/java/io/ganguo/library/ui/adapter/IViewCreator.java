package io.ganguo.library.ui.adapter;

import android.content.Context;

/**
 * 构建器接口
 * <p/>
 * Created by Tony on 4/2/15.
 */
public interface IViewCreator<T> {
    /**
     * 创建ViewHolder
     *
     * @param context
     * @param position
     * @param item
     * @return
     */
    ViewHolder createView(Context context, int position, T item);

    /**
     * 更新ViewHolder
     *
     * @param viewHolder
     * @param position
     * @param item
     */
    void updateView(ViewHolder viewHolder, int position, T item);
}
