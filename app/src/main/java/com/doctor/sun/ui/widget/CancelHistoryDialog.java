package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogCancelHistoryBinding;
import com.doctor.sun.entity.im.TextMsg;

import io.ganguo.library.core.drawable.MaterialProgressDrawable;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.view.View.GONE;

/**
 * 删除聊天记录
 * Created by Lynn on 1/8/16.
 */
public class CancelHistoryDialog extends BaseDialog {
    private DialogCancelHistoryBinding binding;
    private String tid;
    private MaterialProgressDrawable mFooterProgress;
    private Context context;

    public CancelHistoryDialog(Context context, String voipAccount) {
        super(context);
        this.context = context;
        this.tid = voipAccount;
    }

    @Override
    public void beforeInitView() {
        binding = DialogCancelHistoryBinding.inflate(LayoutInflater.from(context));
    }

    @Override
    public void initView() {
        setContentView(binding.getRoot());
        initProgressView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        cancelHistory();
    }

    private void cancelHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<TextMsg> results = realm.where(TextMsg.class)
                        .equalTo("sessionId", tid).findAll();
                realm.beginTransaction();
                results.deleteAllFromRealm();
                realm.commitTransaction();

                cancelComplete();
            }
        }).start();
    }

    private void cancelComplete() {

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFooterProgress != null) {
                    mFooterProgress.stop();
                    binding.ivLoading.setImageDrawable(null);
                }

                binding.ivLoading.setVisibility(GONE);
                Toast.makeText(context, "删除成功!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    private void initProgressView() {
        binding.ivLoading.setVisibility(View.VISIBLE);

        mFooterProgress = new MaterialProgressDrawable(getContext(), binding.getRoot());
        mFooterProgress.setAlpha(255);
        mFooterProgress.setBackgroundColor(Color.TRANSPARENT);
        Resources resources = getContext().getResources();
        int color = resources.getColor(R.color.colorPrimaryDark);
        int blue = resources.getColor(R.color.colorPrimaryDark);
        int green = resources.getColor(R.color.colorPrimaryDark);
        mFooterProgress.setColorSchemeColors(color, blue, green);

        mFooterProgress.start();
    }

    public static void showCancelHistoryDialog(Context context, String voipAccount) {
        CancelHistoryDialog dialog = new CancelHistoryDialog(context, voipAccount);
        dialog.show();
    }
}
