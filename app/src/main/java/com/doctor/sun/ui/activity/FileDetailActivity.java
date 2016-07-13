package com.doctor.sun.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityFileDetailBinding;
import com.doctor.sun.entity.Try;
import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.im.custom.FileTypeMap;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.DownloadUtil;
import com.doctor.sun.util.MD5;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.PermissionUtil;
import com.squareup.otto.Subscribe;

import java.io.File;

import io.ganguo.library.Config;

/**
 * Created by rick on 21/4/2016.
 */
public class FileDetailActivity extends BaseActivity2 {

    public static final String EXTENSION = "EXTENSION";
    public static final String URL = "URL";
    public static final String DURATION = "DURATION";
    public static final String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ActivityFileDetailBinding binding;

    public static Intent makeIntent(Context context, String extension, String url, long duration) {
        Intent intent = new Intent(context, FileDetailActivity.class);
        intent.putExtra(EXTENSION, extension);
        intent.putExtra(URL, url);
        intent.putExtra(DURATION, duration);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_file_detail);
        initView();
        initHeader();
        initListener();
        super.onCreate(savedInstanceState);
    }

    private void initListener() {
        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDownloaded()) {
                    if (haveStoragePermission()) {
                        downloadFile();
                    } else {
                        requestStoragePermission();
                    }
                } else {
                    openFile();
                }
            }
        });
    }

    private void downloadFile() {
        DownloadUtil.downLoadFile(getUrl(), getFile(), new DownloadFinishCallback());
    }

    private boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PermissionUtil.hasSelfPermission(this, STORAGE_PERMISSIONS);
        } else {
            return true;
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(STORAGE_PERMISSIONS, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        binding.setDrawable(FileTypeMap.getDrawable(getExtension()));
        binding.setSize(getFileSizeLabel());
        binding.setIsDownload(isDownloaded());
    }

    private String getExtension() {
        return getStringExtra(EXTENSION);
    }

    private String getFileSizeLabel() {
        long longExtra = getLongExtra(DURATION, 0);
        return String.valueOf(longExtra / 1000) + "KB";
    }

    private void initHeader() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("文件");
        binding.setHeader(header);
    }

    @Subscribe
    public void onDownloadProgress(ProgressEvent event) {
        if (event.getTotalLength() == event.getTotalRead()) {
            NotificationUtil.onFinishDownloadFile(getOpenFileIntent(getFile()));
        } else {
            NotificationUtil.showNotification(event.getTotalRead(), event.getTotalLength());
        }
    }

    public String getLocalPathFor(String url) {
        return MD5.getMessageDigest(url.getBytes());
    }

    public File getFile() {
        return new File(Config.getTempPath(), getLocalPathFor(getUrl()));
    }

    private String getUrl() {
        return getStringExtra(URL);
    }

    public boolean isDownloaded() {
        return getFile().length() == getLongExtra(DURATION, 0);
    }

    public void openFile() {
        try {
            Intent intent = getOpenFileIntent(getFile());

            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(FileDetailActivity.this, "无法打开文件", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private Intent getOpenFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = FileProvider.getUriForFile(this, BuildConfig.FILE_PROVIDER, file);

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension()));
        return intent;
    }

    private class DownloadFinishCallback implements Try {

        public void success() {
            binding.setIsDownload(true);
        }

        @Override
        public void fail() {
            Toast.makeText(FileDetailActivity.this, "无法下载文件,请检测您的网络状态", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onProgressEvent(ProgressEvent e) {
//        if (e.getFrom().equals(getUrl())) {
//            if (e.getTotalLength() == e.getTotalRead()) {
//                binding.progress.setMax(e.getTotalLength());
//                binding.progress.setProgress(e.getTotalRead());
//                binding.progress.setVisibility(View.VISIBLE);
//            } else {
//                binding.progress.setVisibility(View.GONE);
//            }
//        }
    }
}
