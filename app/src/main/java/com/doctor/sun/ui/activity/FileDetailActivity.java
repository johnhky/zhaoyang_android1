package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityFileDetailBinding;
import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.im.custom.FileTypeMap;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.MD5;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.UpdateUtil;
import com.squareup.otto.Subscribe;

import java.util.UUID;

/**
 * Created by rick on 21/4/2016.
 */
public class FileDetailActivity extends BaseActivity2 {

    public static final String EXTENSION = "EXTENSION";
    public static final String URL = "URL";
    public static final String SIZE = "SIZE";
    private ActivityFileDetailBinding binding;

    public static Intent makeIntent(Context context, String extension, String url, String size) {
        Intent intent = new Intent(context, FileDetailActivity.class);
        intent.putExtra(EXTENSION, extension);
        intent.putExtra(URL, url);
        intent.putExtra(SIZE, size);
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
                String url = getStringExtra(URL);
                Log.e(TAG, "download click");
                UpdateUtil.downLoadFile(url,"TempFile" ,new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "download finished");

                    }
                });
            }
        });
    }

    private void initView() {
        binding.setDrawable(FileTypeMap.getDrawable(getStringExtra(EXTENSION)));
        binding.setSize(getStringExtra(SIZE));
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
}
