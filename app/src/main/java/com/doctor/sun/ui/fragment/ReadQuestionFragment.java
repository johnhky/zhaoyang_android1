package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

/**
 * Created by rick on 9/8/2016.
 */
public class ReadQuestionFragment extends AnswerQuestionFragment {

    public static ReadQuestionFragment getInstance(int appointmentId) {

        ReadQuestionFragment fragment = new ReadQuestionFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.DATA, appointmentId);

        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        SortedListAdapter adapter = super.createAdapter();
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
}
