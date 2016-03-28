package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityChattingRecordBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.MsgsAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.DividerItemDecoration;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * 查找聊天记录
 * Created by Lynn on 1/8/16.
 */
public class ChattingRecordActivity extends BaseActivity2 {
    private Logger logger = LoggerFactory.getLogger(ChattingRecordActivity.class);
    private ActivityChattingRecordBinding binding;
    private Realm realm;
    private MsgsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting_record);
        binding.setHeader(getHeaderViewModel());

        initView();
        initListener();
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            //处理输入法返回时隐藏
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackClicked() {
        super.onBackClicked();
    }

    private void initView() {
        binding.rvMsgs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvMsgs.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.shape_divider), true));
    }

    private void initListener() {
        ((EditText) binding.getRoot().findViewById(R.id.et_header)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    binding.rvMsgs.setVisibility(View.VISIBLE);
                    (binding.getRoot().findViewById(R.id.iv_clear)).setVisibility(View.VISIBLE);

                    RealmQuery<TextMsg> query = realm.where(TextMsg.class);
                    query.contains("body", s.toString());
                    RealmResults<TextMsg> results = query.findAll();
                    //result always not null
                    if (results.size() != 0) {
                        mAdapter.clear();
                        mAdapter.addAll(results);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastHelper.showMessage(ChattingRecordActivity.this, "没有找到信息");
                    }
                } else {
                    (binding.getRoot().findViewById(R.id.iv_clear)).setVisibility(View.GONE);
                    binding.rvMsgs.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        (binding.getRoot().findViewById(R.id.iv_clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) binding.getRoot().findViewById(R.id.et_header)).setText("");
            }
        });

        binding.getRoot().findViewById(R.id.et_header).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    private void initData() {
        mAdapter = new MsgsAdapter(ChattingRecordActivity.this,
                (Appointment) getIntent().getParcelableExtra(Constants.PARAM_APPOINTMENT));
        mAdapter.onFinishLoadMore(true);
        binding.rvMsgs.setAdapter(mAdapter);
        realm = Realm.getDefaultInstance();
    }

    private HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this);
    }
}
