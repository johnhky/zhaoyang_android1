package com.doctor.sun.ui.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityImagePreviewBinding;
import com.doctor.sun.util.PermissionUtil;

import java.util.UUID;


/**
 * 图片预览
 * <p/>
 * Created by Lynn on 2/1/16.
 */
public class ImagePreviewActivity extends BaseFragmentActivity2 {
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private ActivityImagePreviewBinding binding;
    private long enqueue;

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
        if (getIntent().getStringExtra(Constants.HEADER).equals("") && isDownloadableLink(getData())) {
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

    private boolean isDownloadableLink(String url) {
        return !url.startsWith("file://");
    }

    private void saveImage() {
        boolean hasSelfPermission = PermissionUtil.hasSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        if (hasSelfPermission) {
            String url = getData();
            if (!url.equals("")) {
                Uri uri = Uri.parse(url);
                DownloadManager.Request r = new DownloadManager.Request(uri);

                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, UUID.randomUUID() + ".png");

                r.allowScanningByMediaScanner();

                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                enqueue = dm.enqueue(r);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == RESULT_OK) {
            saveImage();
        }
    }

    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            // your code
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            try {
                Toast.makeText(ctxt, "保存图片成功", Toast.LENGTH_SHORT).show();
                Uri uriForDownloadedFile = dm.getUriForDownloadedFile(enqueue);
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(uriForDownloadedFile, "image/*");
                startActivity(install);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ctxt, "图片已经保存成功，请安装查看图片的软件来打开图片", Toast.LENGTH_SHORT).show();
            }
            unregisterReceiver(this);
        }
    };
}
