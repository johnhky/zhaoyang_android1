package com.doctor.sun.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityImagePreviewBinding;
import com.doctor.sun.util.SaveImageUtil;


/**
 * 图片预览
 * <p/>
 * Created by Lynn on 2/1/16.
 */
public class ImagePreviewActivity extends BaseFragmentActivity2 {

    private ActivityImagePreviewBinding binding;

    public static Intent makeIntent(Context context, String url) {
        return makeIntent(context, url, "");
    }

    public static Intent makeIntent(Context context, String url, String header) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(Constants.DATA, url);
        intent.putExtra(Constants.HEADER, header);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_preview);
        binding.ivPreview.setDrawingCacheEnabled(true);
        binding.setData(getData());
    }

    private String getData() {
        return getIntent().getStringExtra(Constants.DATA);
    }

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.HEADER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getStringExtra(Constants.HEADER).equals("")) {
            getMenuInflater().inflate(R.menu.menu_save_image, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_image:
                saveImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        Bitmap bitmap = binding.ivPreview.getDrawingCache();
        String imageURL = SaveImageUtil.saveImage(this, bitmap);
        Uri uri = Uri.parse(imageURL);
        showNotification(uri);
    }

    private void showNotification(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pi = PendingIntent.getActivity(this, requestID, intent, flags);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setLights(Color.GREEN, 1000, 3000)
                .setContentTitle("成功保存图片")
                .setContentText("点击查看")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
