package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityWebBrowserBinding;



/**
 * 浏览器
 * <p>
 */
public class HtmlBrowserActivity extends BaseFragmentActivity2 {

    private ActivityWebBrowserBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_browser);
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
        binding.wvContent.setWebChromeClient(new WebChromeClient());
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

//    public void initHeader() {
//        HeaderViewModel header = new HeaderViewModel(this);
//        header.setMidTitle(getStringExtra(Constants.HEADER));
//        binding.headerLayout.setData(header);
//    }

    /**
     * 浏览url内容
     *
     * @param url
     */
    private void loadUrl(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        binding.wvContent.loadDataWithBaseURL(null, url, "text/html", "UTF-8", "");
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
        Intent intent = new Intent(context, HtmlBrowserActivity.class);
        intent.putExtra(Constants.URI, url);
        intent.putExtra(Constants.HEADER, header);
        return intent;
    }

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.HEADER);
    }
}
