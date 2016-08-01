package com.doctor.sun.ui.widget;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogPickImageBinding;
import com.doctor.sun.util.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import io.ganguo.library.Config;
import io.ganguo.library.util.Images;


/**
 * 选择上传图片pop up window
 * 必须要在activity处接受onActivityResult
 * Created by lucas on 1/5/16.
 */
public class PickImageDialog extends BottomSheetDialog {
    public static final String TAG = PickImageDialog.class.getSimpleName();
    public static final int CAMERA_MASK = 1 << 15;
    public static final int REQUEST_CODE_MASK = ~CAMERA_MASK;

    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            "android.permission.READ_EXTERNAL_STORAGE"};

    private int imageRequestCode;
    private Activity mActivity;
    private DialogPickImageBinding binding;

    public PickImageDialog(Context context, int imageRequestCode) {
        super(context);
        if (isCameraRequest(imageRequestCode)) {
            throw new IllegalArgumentException("Can only use lower 15 bits for pick image imageRequestCode");
        }
        this.imageRequestCode = imageRequestCode;
        mActivity = (AppCompatActivity) context;
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_pick_image, null, false);
        setCanceledOnTouchOutside(false);
        setContentView(binding.getRoot());
        View parent = (View) binding.getRoot().getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        binding.getRoot().measure(0, 0);
        behavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);

        initListener();
    }


    private void initListener() {
        binding.tvCamera.setOnClickListener(onCameraClick());

        binding.tvGallery.setOnClickListener(onGalleryPick());

        binding.tvCancel.setOnClickListener(onCancel());
    }

    @NonNull
    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    @NonNull
    private View.OnClickListener onGalleryPick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(mActivity, imageRequestCode);
                dismiss();
            }
        };
    }

    @NonNull
    public View.OnClickListener onCameraClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(mActivity, imageRequestCode);
                dismiss();
            }
        };
    }

    public static void openGallery(Activity mActivity, int imageRequestCode) {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        mActivity.startActivityForResult(intentFromGallery, imageRequestCode);
    }


    public static void openCamera(final Activity mActivity, final int imageRequestCode) {
        checkPermission(mActivity, new Runnable() {
            @Override
            public void run() {
                try {
                    final Uri image = getFileUrlForCameraRequest(mActivity);
                    Intent intentFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ClipData clip = ClipData.newRawUri(null, image);
                        intentFromCamera.setClipData(clip);
                    }
                    intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, image);
                    intentFromCamera.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intentFromCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mActivity.startActivityForResult(intentFromCamera, imageRequestCode | CAMERA_MASK);
                } catch (Exception e) {
                    Toast.makeText(mActivity, "无法打开拍摄应用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static Uri getFileUrlForCameraRequest(Context mActivity) {
        return FileProvider.getUriForFile(mActivity, BuildConfig.FILE_PROVIDER, handleCameraRequest());
    }

    public static File handleRequest(Context context, Intent data, int requestCode) {
        File result;
        if (!isCameraRequest(requestCode)) {
            result = compressImage(handleGalleryRequest(context, data));
        } else {
            result = compressImage(handleCameraRequest());
        }
        return result;
    }

    private static boolean isCameraRequest(int requestCode) {
        return ((requestCode >> 15) & 0x1) == 1;
    }

    public static int getRequestCode(int requestCode) {
        return requestCode & REQUEST_CODE_MASK;
    }

    @NonNull
    private static File handleCameraRequest() {
        return new File(Config.getTempPath(), "imageFromCamera");
    }

    private static File handleGalleryRequest(Context context, Intent data) {
        Uri selectedImage = data.getData();
        return handleImageUrl(context, selectedImage);
    }

    @Nullable
    public static File handleImageUrl(Context context, Uri selectedImage) {
        File file;
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

        if (file.length() == 0) {
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
        Log.d("image inbetweenItemCount", "compressImage: " + quality);
        Bitmap smallBitmap = Images.getSmallBitmap(file.getPath());
        try {
            Bitmap rotatedBmp = Images.getCorrectOrientationBitmap(file, smallBitmap);
            Images.saveJPEG(rotatedBmp, (int) quality, to);
            return to;
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }

    /**
     * Start the dialog and display it on screen.  The window is placed in the
     * application layer and opaque.  Note that you should not override this
     * method to do initialization when the dialog is shown, instead implement
     * that in {@link #onStart}.
     */
    @Override
    public void show() {
        boolean hasPermission = PermissionUtil.hasSelfPermission(mActivity,
                PERMISSIONS);
        if (hasPermission) {
            super.show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public static void checkPermission(Activity mActivity, Runnable runnable) {
        boolean hasPermission = PermissionUtil.hasSelfPermission(mActivity,
                PERMISSIONS);
        if (hasPermission) {
            runnable.run();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
            }
        }
    }
}
