package com.doctor.sun.ui.activity.patient;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityMyQrCodeBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import com.doctor.sun.util.PermissionUtil;

/**
 * Created by rick on 20/5/2016.
 */
public class MyQrCodeActivity extends BaseFragmentActivity2 {
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private ProfileModule api = Api.of(ProfileModule.class);
    private String data = "";

    private ActivityMyQrCodeBinding binding;

    public static Intent intentFor(Context context, Doctor doctor) {
        Intent intent = new Intent(context, MyQrCodeActivity.class);
        intent.putExtra(Constants.DATA, doctor);
        return intent;
    }

    public Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_qr_code);
        binding.setData(getData());
        loadData();
    }

    public void loadData() {
        api.barcode().enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                data = response;
                binding.setQrcode(response);
            }
        });
    }

    public void onMenuClicked() {
        boolean hasSelfPermission = PermissionUtil.hasSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        if (hasSelfPermission) {
            if (!data.equals("")) {
                Uri uri = Uri.parse(data);
                DownloadManager.Request r = new DownloadManager.Request(uri);

                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "我的二维码.png");

                r.allowScanningByMediaScanner();

                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);
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
        if (grantResults[0]== RESULT_OK) {
            onMenuClicked();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_locally, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_locally: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_my_qrcode;
    }
}
