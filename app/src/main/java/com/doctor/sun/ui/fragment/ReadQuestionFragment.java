package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by rick on 9/8/2016.
 */
public class ReadQuestionFragment extends AnswerQuestionFragment {

    public static ReadQuestionFragment getInstance(int appointmentId, boolean readOnly) {

        ReadQuestionFragment fragment = new ReadQuestionFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.DATA, appointmentId);
        args.putBoolean(Constants.READ_ONLY, readOnly);

        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        final boolean isReadOnly = getArguments().getBoolean(Constants.READ_ONLY, false);
        SortedListAdapter adapter = super.createAdapter();
        adapter.setConfig(AdapterConfigKey.IS_READ_ONLY, isReadOnly);
        adapter.setLayoutIdInterceptor(new SortedListAdapter.LayoutIdInterceptor() {
            @Override
            public int intercept(int origin) {
                switch (origin) {
                    case R.layout.new_item_options: {
                        return R.layout.new_r_item_options;
                    }
                    case R.layout.new_item_options_dialog: {
                        return R.layout.item_r_options_dialog;
                    }
                    case R.layout.new_item_options_rect: {
                        return R.layout.item_r_options_rect;
                    }
                    case R.layout.item_further_consultation: {
                        return R.layout.item_r_further_consultation;
                    }
                    case R.layout.item_pick_date3: {
                        return R.layout.item_r_pick_date3;
                    }
                    case R.layout.item_pick_question_time: {
                        return R.layout.item_r_pick_time;
                    }
                    case R.layout.item_text_input6: {
                        return R.layout.item_r_text_input6;
                    }
                    case R.layout.item_prescription3: {
                        return R.layout.item_r_prescription;
                    }
                    case R.layout.item_hospital: {
                        return R.layout.item_r_hospital;
                    }
                    case R.layout.item_reminder2: {
                        return R.layout.item_r_reminder2;
                    }
                    case R.layout.item_view_image: {
                        return R.layout.item_r_view_image;
                    }
                    case R.layout.item_question2: {
                        return R.layout.item_r_question2;
                    }
                    case R.layout.item_add_reminder:
                    case R.layout.item_pick_image:
                    case R.layout.item_add_prescription3: {
                        return R.layout.item_empty;
                    }
                }
                return origin;
            }
        });
        return adapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final boolean isReadOnly = getArguments().getBoolean(Constants.READ_ONLY, false);
        if (!isReadOnly) {
            inflater.inflate(R.menu.menu_remind, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remind: {
                QuestionModule api = Api.of(QuestionModule.class);
                ArrayList<String> need_fill = new ArrayList<>();
                for (int i = 0; i < getAdapter().size(); i++) {
                    SortedItem sortedItem = getAdapter().get(i);
                    if (sortedItem.isUserSelected()) {
                        need_fill.add(sortedItem.getKey());
                    }
                }
                if (need_fill.isEmpty()) {
                    Toast.makeText(getContext(), "请选择需要患者重填的问题", Toast.LENGTH_SHORT).show();
                    return true;
                }
                api.refill(appointmentId + "",
                        need_fill).enqueue(new ApiCallback<List<Answer>>() {
                    @Override
                    protected void handleResponse(List<Answer> response) {
                        ToastHelper.showMessage(getContext(), "保存重填数据成功");
                    }

                    @Override
                    protected void handleApi(ApiDTO<List<Answer>> body) {
                        ToastHelper.showMessage(getContext(), "保存重填数据失败, 请检查网络设置");
                        super.handleApi(body);
                    }
                });

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
