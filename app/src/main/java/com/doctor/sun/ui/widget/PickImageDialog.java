package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.doctor.sun.databinding.DialogAvaterBinding;
import com.doctor.sun.event.CloseDialogEvent;
import com.doctor.sun.util.PermissionsUtil;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import io.ganguo.library.Config;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Images;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 选择上传图片pop up window
 * Created by lucas on 1/5/16.
 */
public class PickImageDialog extends BaseDialog {
    private Logger logger = LoggerFactory.getLogger(PickImageDialog.class);
    private Context mActivity;
    private DialogAvaterBinding binding;
    private GetActionButton getActionButton;
    private PermissionsUtil permissionsUtil;

    public PickImageDialog(Context context, GetActionButton getActionButton) {
        super(context);
        mActivity = context;
        this.getActionButton = getActionButton;
    }

    @Override
    public void beforeInitView() {
        binding = DialogAvaterBinding.inflate(LayoutInflater.from(mActivity));
        setContentView(binding.getRoot());
    }

    @Override
    public void initView() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = Systems.getScreenWidth(mActivity);
        getWindow().setAttributes(lp);
    }

    @Override
    public void initListener() {
        binding.tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionButton.onClickCamera();
                dismiss();
            }
        });

        binding.tvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionButton.onClickAlbum();
                dismiss();
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void initData() {
        permissionsUtil = new PermissionsUtil((Activity) mActivity);
        permissionsUtil.checkPermissionAndRequest((Activity) mActivity,
                PermissionsUtil.PERMISSION_CAMERA, PermissionsUtil.PERMISSION_RES);
    }


    public interface GetActionButton {
        void onClickCamera();

        void onClickAlbum();
    }

    public static void showAvatarDialog(Context context, final GetActionButton getActionButtom) {
        final PickImageDialog avatarDialog = new PickImageDialog(context, getActionButtom);
        avatarDialog.show();
    }

    public static void chooseImage(final Activity activity, final int requestCode) {
        final Uri image = Uri.fromFile(handleCameraRequest());
        PickImageDialog.showAvatarDialog(activity, new PickImageDialog.GetActionButton() {
            @Override
            public void onClickCamera() {
                Intent intentFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, image);
                activity.startActivityForResult(intentFromCamera, requestCode / 2);
            }

            @Override
            public void onClickAlbum() {
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_PICK);
                activity.startActivityForResult(intentFromGallery, requestCode);
            }
        });
    }

    @NonNull
    public static File handleCameraRequest() {
        return new File(Config.getImagePath(), "image");
    }

    public static File handleAlbumRequest(Context context, Intent data) {
        File file;
        Uri selectedImage = data.getData();
        //log: selectedImage.getScheme() --> file / content
        switch (selectedImage.getScheme()) {
            case "content": {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                cursor.close();
                file = new File(picturePath);
                break;
            }
            case "file": {
                file = new File(selectedImage.getPath());
                break;
            }
            default: {
                file = null;
            }
        }
        return file;
    }

    @Nullable
    public static File compressImage(File file) {
        File to = new File(Config.getImageCachePath(), String.valueOf("/" + UUID.randomUUID()));
        try {
            boolean newFile = to.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }

        //2Mb 为基准压缩计算比率图片
        long quality = (long) 1024 * 1024 * 2 * 100 / file.length();
        if (quality > 100) {
            quality = 100;
        }
        if (quality < 10) {
            quality = 10;
        }
        Log.d("image size", "compressImage: " + quality);
        Bitmap smallBitmap = Images.getSmallBitmap(file.getPath());
        try {
            Images.saveJPEG(smallBitmap, (int) quality, to);
            return to;
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }

    @Subscribe
    public void onCloseEvent(CloseDialogEvent event) {
        if (event.isClose()) {
            dismiss();
        }
    }
}
