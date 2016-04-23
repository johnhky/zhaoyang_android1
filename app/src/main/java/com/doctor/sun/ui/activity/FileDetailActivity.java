package com.doctor.sun.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityFileDetailBinding;
import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.im.custom.FileTypeMap;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.MD5;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.UpdateUtil;
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
                File file = getFile();
                if (!isDownloaded()) {
                    UpdateUtil.downLoadFile(getUrl(), file, new DownloadFinishCallback());
                } else {
                    openFile(file);
                }
            }
        });
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
        NotificationUtil.showNotification(event.getTotalRead(), event.getTotalLength());
    }

    public String getLocalPathFor(String url) {
        return MD5.getMessageDigest(url.getBytes());
    }

    public File getFile() {
        return new File(Config.getDataPath(), getLocalPathFor(getUrl()));
    }

    private String getUrl() {
        return getStringExtra(URL);
    }

    public boolean isDownloaded() {
        return getFile().length() == getLongExtra(DURATION, 0);
    }

    public void openFile(File file) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");

            intent.addCategory("android.intent.category.DEFAULT");

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri uri = Uri.fromFile(file);

            intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension()));

            startActivity(intent);
        }catch (ActivityNotFoundException e) {
            Toast.makeText(FileDetailActivity.this, "无法打开文件", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFinishCallback implements Runnable {

        @Override
        public void run() {
            binding.setIsDownload(true);
        }
    }
}
