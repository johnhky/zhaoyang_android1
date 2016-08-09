package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityWebBrowserBinding;
import com.doctor.sun.ui.model.HeaderViewModel;


/**
 * 浏览器
 * <p>
 */
public class WebBrowserActivity extends BaseFragmentActivity2 {

    private ActivityWebBrowserBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }

    public void initWebView() {
        binding.wvContent.setWebViewClient(createWebViewClient());
        WebSettings settings = binding.wvContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // 缩放文字
            settings.setTextZoom(95);
        }
        loadUrl(getIntent().getStringExtra(Constants.URI));
    }

    @NonNull
    public WebViewClient createWebViewClient() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadUrl(url);
                return true;//停止在当前界面
            }
        };
    }


    /**
     * 浏览url内容
     *
     * @param url
     */
    private void loadUrl(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        binding.wvContent.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != binding.wvContent) {
            binding.wvContent.clearView();
            binding.wvContent.stopLoading();
            binding.wvContent.removeAllViews();
            binding.wvContent.destroy();
        }
    }

    public static Intent intentFor(Context context, String url, String header) {
        Intent intent = new Intent(context, WebBrowserActivity.class);
        intent.putExtra(Constants.URI, url);
        intent.putExtra(Constants.HEADER, header);
        return intent;
    }

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.HEADER);
    }
}
