package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.widget.PickImageDialog;

import java.io.File;


/**
 * Created by rick on 21/3/2016.
 */
public class PickImageViewModel implements LayoutId {
    public static final int REQUEST_CODE = 100;
    private String src;
    private int itemLayoutId;

    public PickImageViewModel(String src, int itemLayoutId) {
        this.src = src;
        this.itemLayoutId = itemLayoutId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public View.OnClickListener pickImage(final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog dialog = new PickImageDialog(v.getContext(), vh.getAdapterPosition() + 1);
                dialog.show();
            }
        };
    }

    public View.OnClickListener pickImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog dialog = new PickImageDialog(v.getContext(), REQUEST_CODE);
                dialog.show();
            }
        };
    }

    public static String handleRequest(Context context, Intent data, int requestCode) {
        File file = PickImageDialog.handleRequest(context, data, requestCode);
        String path = "file://" + file.getAbsolutePath();
        return path;
    }

    public static void handleRequest(Context context, SimpleAdapter<PickImageViewModel, ViewDataBinding> adapter, Intent data, int requestCode) {
        int position = PickImageDialog.getRequestCode(requestCode) - 1;
        File file = PickImageDialog.handleRequest(context, data, requestCode);
        String path = "file://" + file.getAbsolutePath();

        // 假如点击的是最后一个item,而且总item不足3个,就在itemCount前一位insert一个ViewModel
        if (position == adapter.getItemCount() - 1 && position != 2) {
            int location = adapter.getItemCount() - 1;
            adapter.add(location, new PickImageViewModel(path, R.layout.item_pick_image));
            adapter.notifyItemInserted(location);
        } else {
            adapter.get(position).setSrc(path);
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }
}
