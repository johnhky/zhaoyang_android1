package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.Observable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
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

import io.ganguo.library.common.LoadingHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


/**
 * Created by rick on 21/3/2016.
 */
public class ItemPickImage extends BaseItem {
    public static final String TAG = ItemPickImage.class.getSimpleName();
    public static final int DEFAULT_IMAGE_CONSTRAIN = 5;
    private String src = "";
    private String localPath = "";
    private int itemSizeConstrain = DEFAULT_IMAGE_CONSTRAIN;
    private int itemSize = 0;

    public ItemPickImage(int itemLayoutId, String path) {
        super(itemLayoutId);
        this.src = path;
        setSpan(4);
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

//    public String getImageToLoad() {
//        return src;
//    }

    public boolean onLongClick(Context context, final SortedListAdapter adapter) {
        if (src == null || src.equals("")) {
            return true;
        } else {
            TwoChoiceDialog.show(context, "是否删除图片?", "取消", "确定", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(MaterialDialog dialog) {
                    //删除对应图片
                    dialog.dismiss();
                    notifyPropertyChanged(BR.removed);
                    adapter.removeItem(ItemPickImage.this);
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
        PickImageDialog dialog = new PickImageDialog(context, vh.getAdapterPosition());
        dialog.show();
    }

    public static long getPositionForNewImage(SortedListAdapter adapter, ItemPickImage item) {
        int distance = adapter.inBetweenItemCount(item.getKey(), item.getKey().replace(QuestionType.upImg, ""));
        item.setItemSize(distance);
        return item.getPosition() - QuestionsModel.PADDING + 2 + distance;
    }

    public static void handleRequest(Context context, final SortedListAdapter adapter, Intent data, int requestCode) {
        File file = PickImageDialog.handleRequest(context, data, requestCode);
        String path = "file://" + file.getAbsolutePath();

        int pickerPosition = PickImageDialog.getRequestCode(requestCode);
        ItemPickImage pickerItem = (ItemPickImage) adapter.get(pickerPosition);

        final ItemPickImage item = new ItemPickImage(R.layout.item_pick_image, "");
        item.setLocalPath(path);
        item.setPosition(getPositionForNewImage(adapter, pickerItem));
        item.setItemId(UUID.randomUUID().toString());

        pickerItem.registerItemChangedListener(item);
        adapter.insert(item);


        LoadingHelper.showMaterLoading(context, "正在上传图片");
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-intent"), file);
        ToolModule api = Api.of(ToolModule.class);
        api.uploadPhoto(body).enqueue(new SimpleCallback<Photo>() {

            @Override
            protected void handleResponse(Photo response) {
                item.setSrc(response.getUrl());
                adapter.insert(item);
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFailure(Call<ApiDTO<Photo>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });

    }


    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (src == null || src.equals("")) {
            int adapterPosition = adapter.indexOfImpl(this);
            int distance = adapter.inBetweenItemCount(adapterPosition, getKey().replace(QuestionType.upImg, ""));
            if (distance <= 1) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = distance; i > 1; i--) {
                int index = adapterPosition - i + 1;
                try {
                    ItemPickImage sortedItem = (ItemPickImage) adapter.get(index);
                    String src = sortedItem.getSrc();
                    if (src != null && !src.equals("")) {
                        sb.append(src);
                        if (i != 2) {
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

    @Override
    public int getLayoutId() {
        if (itemSize > itemSizeConstrain) {
            return R.layout.item_empty;
        }
        return super.getLayoutId();
    }

    public int getItemSizeConstrain() {
        return itemSizeConstrain;
    }

    public void setItemSizeConstrain(int itemSizeConstrain) {
        this.itemSizeConstrain = itemSizeConstrain;
    }

    public void registerItemChangedListener(BaseItem parcelable) {
        parcelable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == com.android.databinding.library.baseAdapters.BR.removed) {
                    itemSize -= 1;
                    notifyChange();
                }
            }
        });
    }

    @Bindable
    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
        notifyPropertyChanged(BR.itemSize);
    }
}
