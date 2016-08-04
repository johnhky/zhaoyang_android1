package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by rick on 21/3/2016.
 */
public class ItemPickImage extends BaseItem {
    public static final String TAG = ItemPickImage.class.getSimpleName();
    private String src;
    private String localPath = "";

    public ItemPickImage(int itemLayoutId, String path) {
        super(itemLayoutId);
        this.src = path;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getImageToLoad() {
        if (localPath != null && !localPath.equals("")) {
            return localPath;
        } else {
            return src;
        }
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
                    adapter.remove(ItemPickImage.this);
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
        int distance = adapter.inBetweenItemCount(vh.getAdapterPosition(), getKey().replace(QuestionType.upImg, ""));
        int position = getPosition() - QuestionsModel.PADDING + 2 + distance;
        PickImageDialog dialog = new PickImageDialog(context, position);
        dialog.show();
    }

    public static void handleRequest(Context context, final SortedListAdapter adapter, Intent data, int requestCode) {
        File file = PickImageDialog.handleRequest(context, data, requestCode);
        String path = "file://" + file.getAbsolutePath();
        final ItemPickImage item = new ItemPickImage(R.layout.item_pick_image, "");
        item.setLocalPath(path);
        item.setPosition(PickImageDialog.getRequestCode(requestCode));
        item.setItemId(UUID.randomUUID().toString());
        adapter.insert(item);

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-intent"), file);
        ToolModule api = Api.of(ToolModule.class);
        api.uploadPhoto(body).enqueue(new SimpleCallback<Photo>() {

            @Override
            protected void handleResponse(Photo response) {
                item.setSrc(response.getUrl());
                adapter.insert(item);
            }
        });

    }


    @Override
    public int getSpan() {
        return 4;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (src == null || src.equals("")) {
            int adapterPosition = adapter.indexOf(this);
            int distance = adapter.inBetweenItemCount(adapterPosition, getKey().replace(QuestionType.upImg, ""));
            if (distance <= 1) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = distance; i > 0; i--) {
                int index = adapterPosition - distance + 1;
                try {
                    ItemPickImage sortedItem = (ItemPickImage) adapter.get(index);
                    String src = sortedItem.getSrc();
                    sb.append(src);
                    sb.append(",");
                    if (src != null && !src.equals("")) {
                        sb.append(src);
                        if (i != 1) {
                            sb.append(",");
                        }
                    }
                } catch (ClassCastException ignored) {
                    ignored.printStackTrace();
                }
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("question_id", getKey().replace(QuestionType.upImg, ""));
            String content = sb.toString();
            result.put("fill_content", content);
            return result;
        }
        return null;
    }
}