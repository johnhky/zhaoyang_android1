package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
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
        SaveImageUtil.saveImage(this, bitmap);
        Toast.makeText(this, "成功保存图片", Toast.LENGTH_SHORT).show();
    }
}
