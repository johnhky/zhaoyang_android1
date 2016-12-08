package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.RefreshQuestionsEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.doctor.TemplatesInventoryActivity;
import com.doctor.sun.ui.adapter.MapLayoutIdInterceptor;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.doctor.sun.ui.adapter.core.AdapterConfigKey.IS_READ_ONLY;

/**
 * Created by rick on 9/8/2016.
 */
@Factory(type = BaseFragment.class, id = "ReadQuestionsFragment")
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
        adapter.putBoolean(AdapterConfigKey.IS_READ_ONLY, isReadOnly || !isEditMode);
        adapter.putBoolean(AdapterConfigKey.IS_DONE, isReadOnly);
        adapter.setLayoutIdInterceptor(new ReadOnlyQuestionsInterceptor(isReadOnly));
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
                getAdapter().putBoolean(AdapterConfigKey.IS_READ_ONLY, false);
                getAdapter().notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();
                return true;
            }
            case R.id.action_send_remind: {
                sendRemind();
                return true;
            }
            case R.id.action_add_question: {
                Intent intent = TemplatesInventoryActivity.intentFor(getContext(),
                        getArguments().getString(Constants.DATA));
                startActivity(intent);
                return true;
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
            getActivity().getMenuInflater().inflate(R.menu.menu_remind_refill, menu);
            if (!QuestionsPath.SCALES.equals(getQuestionsPath())) {
                getActivity().getMenuInflater().inflate(R.menu.menu_asign_questions, menu);
            }
        } else {
            getActivity().getMenuInflater().inflate(R.menu.menu_send_remind, menu);
            if (!QuestionsPath.SCALES.equals(getQuestionsPath())) {
                getActivity().getMenuInflater().inflate(R.menu.menu_asign_questions, menu);
            }
        }
    }

    public boolean sendRemind() {
        QuestionModule api = Api.of(QuestionModule.class);
        final ArrayList<String> need_fill = new ArrayList<>();
        final ArrayList<String> need_fill_key = new ArrayList<>();
        for (int i = 0; i < getAdapter().size(); i++) {
            SortedItem item = getAdapter().get(i);
            if (item instanceof Questions2) {
                Questions2 questions = (Questions2) item;
                if (questions.isUserSelected()) {
                    need_fill.add(questions.answerId);
                    need_fill_key.add(questions.getKey());
                }
            }
        }
        if (need_fill.isEmpty()) {
            Toast.makeText(getContext(), "请选择需要患者重填的问题", Toast.LENGTH_SHORT).show();
            return true;
        }
        api.refill2(getQuestionsPath(), id,
                need_fill).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                makeText(getContext(), "保存重填数据成功", LENGTH_SHORT).show();
                isEditMode = false;
                for (String s : need_fill_key) {
                    Questions2 question = (Questions2) getAdapter().get(s);
                    if (question != null) {
                        question.refill = 1;
                    }
                }
                getAdapter().putBoolean(IS_READ_ONLY, true);
                getAdapter().notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                if (getContext() == null) {
                    return;
                }
                makeText(getContext(), "保存重填数据失败, 请检查网络设置", LENGTH_SHORT).show();
                super.onFailure(call, t);
            }
        });
        return false;
    }

    public String getQuestionsPath() {
        return getArguments().getString(Constants.PATH);
    }

    @Subscribe
    public void onEventMainThread(RefreshQuestionsEvent event) {
        if (event.getId().equals(getAppointmentId())) {
            getAdapter().clear();
            loadMore();
        }
    }

    public static class ReadOnlyQuestionsInterceptor extends MapLayoutIdInterceptor {

        public ReadOnlyQuestionsInterceptor(boolean isReadOnly) {

            put(R.layout.item_options, R.layout.item_r_options);

            put(R.layout.item_options_dialog, R.layout.item_r_options_dialog);

            put(R.layout.item_options_rect_input, R.layout.item_r_options_rect_input);

            put(R.layout.item_options_rect, R.layout.item_r_options_rect);

            put(R.layout.item_further_consultation, R.layout.item_r_further_consultation);

            put(R.layout.item_pick_date3, R.layout.item_r_pick_date3);

            put(R.layout.item_pick_question_time, R.layout.item_r_pick_time);

            put(R.layout.item_text_input6, R.layout.item_r_text_input6);

            put(R.layout.item_prescription3, R.layout.item_r_prescription);

            put(R.layout.item_pick_hospital, R.layout.item_r_hospital);

            put(R.layout.item_reminder2, R.layout.item_r_reminder2);

            put(R.layout.item_view_image, R.layout.item_r_view_image);

            put(R.layout.item_question, R.layout.item_r_question);

            if (Settings.isDoctor() || isReadOnly) {
                put(R.layout.item_scales, R.layout.item_r_scales);
            }

            put(R.layout.item_options_load_prescription, R.layout.item_empty);

            put(R.layout.item_add_reminder, R.layout.item_empty);

            put(R.layout.item_pick_image, R.layout.item_empty);

            put(R.layout.item_add_prescription3, R.layout.item_empty);
        }
    }

    @Override
    public ShowFABEvent getShowFABEvent() {
        return new ShowFABEvent(getAppointmentId());
    }

    @Override
    public HideFABEvent getHideFABEvent() {
        return new HideFABEvent(getAppointmentId());
    }
}
