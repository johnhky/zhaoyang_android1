package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.io.File;
import java.util.UUID;


/**
 * Created by rick on 21/3/2016.
 */
public class PickImageViewModel extends BaseItem {
    private String src;

    public PickImageViewModel(int itemLayoutId, String path) {
        super(itemLayoutId);
        this.src = path;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean onLongClick(Context context, final SortedListAdapter adapter) {
        if (src == null || src.equals("")) {
            return true;
        } else {
            TwoChoiceDialog.show(context, "是否删除图片?", "取消", "确定", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(MaterialDialog dialog) {
                    //删除对应图片
                    dialog.dismiss();
                    adapter.remove(PickImageViewModel.this);
                }

                @Override
                public void onCancelClick(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            });
            return false;
        }
    }

    public void onClick(Context context, SortedListAdapter adapter, BaseViewHolder vh) {
        if (src == null || src.equals("")) {
            pickImage(context, adapter, vh);
        } else {
            context.startActivity(
                    ImagePreviewActivity.makeIntent(context, src));
        }
    }

    public void pickImage(Context context, SortedListAdapter adapter, BaseViewHolder vh) {
        int distance = adapter.inBetweenItemCount(vh.getAdapterPosition(), getKey().replace("pick_image", ""));
        int position = getPosition() - QuestionsModel.PADDING + 2 + distance;
        PickImageDialog dialog = new PickImageDialog(context, position);
        dialog.show();
    }

    public static void handleRequest(Context context, SortedListAdapter adapter, Intent data, int requestCode) {
        File file = PickImageDialog.handleRequest(context, data, requestCode);
        String path = "file://" + file.getAbsolutePath();
        PickImageViewModel item = new PickImageViewModel(R.layout.item_pick_image, path);
        item.setPosition(PickImageDialog.getRequestCode(requestCode));
        item.setItemId(UUID.randomUUID().toString());
        adapter.insert(item);
    }

    @Override
    public float getSpan() {
        return 0.25f;
    }

    @Override
    public String toJson(SortedListAdapter adapter) {
        if (src == null || src.equals("")) {
            return "\"}";
        }
        return src + ",";
    }
}
