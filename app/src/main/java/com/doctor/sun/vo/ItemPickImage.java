package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;

import java.io.File;

import io.ganguo.library.common.LoadingHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by rick on 18/8/2016.
 */

public class ItemPickImage extends BaseItem {
    private String localPath;
    private String src;

    public ItemPickImage(int itemLayoutId, String src) {
        super(itemLayoutId);
        this.src = src;
    }

    public String getImageToLoad() {
        if (localPath != null && !localPath.equals("")) {
            return localPath;
        } else {
            return src;
        }
    }

    public boolean hasImage() {
        String imageToLoad = getImageToLoad();
        return imageToLoad != null && !imageToLoad.equals("");
    }

    public void pickImage(Context context, BaseViewHolder vh) {
        PickImageDialog dialog = new PickImageDialog(context, vh.getAdapterPosition());
        dialog.show();
    }

    public static void handleRequest(Context context, final SortedListAdapter adapter, Intent data, int requestCode) {
        File file = PickImageDialog.handleRequest(context, data, requestCode);
        String path = "file://" + file.getAbsolutePath();

        int pickerPosition = PickImageDialog.getRequestCode(requestCode);
        SortedItem sortedItem = adapter.get(pickerPosition);
        boolean itemIsPicker = sortedItem != null && sortedItem instanceof ItemPickImage;
        if (!itemIsPicker) {
            return;
        }
        final ItemPickImage pickerItem = (ItemPickImage) sortedItem;

        pickerItem.setLocalPath(path);

        LoadingHelper.showMaterLoading(context, "正在上传图片");
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-intent"), file);
        ToolModule api = Api.of(ToolModule.class);
        api.uploadPhoto(body).enqueue(new SimpleCallback<Photo>() {

            @Override
            protected void handleResponse(Photo response) {
                pickerItem.setSrc(response.getUrl());
                pickerItem.notifyChange();
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFailure(Call<ApiDTO<Photo>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });

    }

    @Bindable
    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
        notifyPropertyChanged(BR.localPath);
    }

    @Bindable
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        notifyPropertyChanged(BR.src);
    }

    @Override
    public String getValue() {
        return src;
    }
}
