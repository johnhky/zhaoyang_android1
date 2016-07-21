package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityImagePreviewBinding;
import com.doctor.sun.ui.model.HeaderViewModel;


/**
 * 图片预览
 * <p/>
 * Created by Lynn on 2/1/16.
 */
public class ImagePreviewActivity extends BaseActivity2 {
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
        initView();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackClicked() {
        super.onBackClicked();
    }

    private void initView() {
        ActivityImagePreviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_image_preview);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle(getStringExtra(Constants.HEADER));
        binding.setHeader(header);
        binding.setData(getData());
    }

    private String getData() {
        return getIntent().getStringExtra(Constants.DATA);
    }
}
