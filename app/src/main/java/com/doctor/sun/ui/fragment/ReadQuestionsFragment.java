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
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;

import io.ganguo.library.common.ToastHelper;
import retrofit2.Call;

/**
 * Created by rick on 9/8/2016.
 */
public class ReadQuestionsFragment extends AnswerQuestionFragment {
    public static final String TAG = ReadQuestionsFragment.class.getSimpleName();

    private boolean isEditMode = false;

    public static ReadQuestionsFragment getInstance(String id, String path, boolean readOnly) {
        return getInstance(id, path, "", readOnly);
    }

    public static ReadQuestionsFragment getInstance(String id, @QuestionsPath String path, String questionType, boolean readOnly) {

        ReadQuestionsFragment fragment = new ReadQuestionsFragment();

        Bundle args = getArgs(id, path, questionType, readOnly);

        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static Bundle getArgs(String id, @QuestionsPath String path, String questionType, boolean readOnly) {
        Bundle args = new Bundle();
        args.putString(Constants.FRAGMENT_NAME, TAG);
        args.putString(Constants.DATA, id);
        args.putString(Constants.PATH, path);
        args.putString(Constants.QUESTION_TYPE, questionType);
        args.putBoolean(Constants.READ_ONLY, readOnly);
        return args;
    }

    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        final boolean isReadOnly = getArguments().getBoolean(Constants.READ_ONLY, false);
        SortedListAdapter adapter = super.createAdapter();
        adapter.setConfig(AdapterConfigKey.IS_READ_ONLY, isReadOnly || !isEditMode);
        adapter.setConfig(AdapterConfigKey.IS_DONE, isReadOnly);
        adapter.setLayoutIdInterceptor(new SortedListAdapter.LayoutIdInterceptor() {
            @Override
            public int intercept(int origin) {
                switch (origin) {
                    case R.layout.item_options: {
                        return R.layout.item_r_options;
                    }
                    case R.layout.item_options_dialog: {
                        return R.layout.item_r_options_dialog;
                    }
                    case R.layout.item_options_rect_input: {
                        return R.layout.item_r_options_rect_input;
                    }
                    case R.layout.item_options_rect: {
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
                    case R.layout.item_scales: {
                        return R.layout.item_r_scales;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_mode: {
                isEditMode = true;
                getAdapter().setConfig(AdapterConfigKey.IS_READ_ONLY, false);
                getAdapter().notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();
                return true;
            }
            case R.id.action_send_remind: {
                if (sendRemind()) return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final boolean isReadOnly = getArguments().getBoolean(Constants.READ_ONLY, false);
        if (isReadOnly) {
            return;
        }
        if (!isEditMode) {
            getActivity().getMenuInflater().inflate(R.menu.menu_edit_remind, menu);
        } else {
            getActivity().getMenuInflater().inflate(R.menu.menu_send_remind, menu);
        }
    }

    public boolean sendRemind() {
        QuestionModule api = Api.of(QuestionModule.class);
        final ArrayList<String> need_fill = new ArrayList<>();
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
        api.refill2(getType(), id,
                need_fill).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                ToastHelper.showMessage(getContext(), "保存重填数据成功");
                isEditMode = false;
                for (String s : need_fill) {
                    Questions2 question = (Questions2) getAdapter().get(s);
                    question.refill = 1;
                }
                getAdapter().setConfig(AdapterConfigKey.IS_READ_ONLY, true);
                getAdapter().notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                ToastHelper.showMessage(getContext(), "保存重填数据失败, 请检查网络设置");
                super.onFailure(call, t);
            }
        });
        return false;
    }

    public String getType() {
        return getArguments().getString(Constants.PATH);
    }
}
