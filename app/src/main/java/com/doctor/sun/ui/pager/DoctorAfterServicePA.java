package com.doctor.sun.ui.pager;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.fragment.EditForumFragment;
import com.doctor.sun.ui.fragment.ViewForumFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 6/6/2016.
 */
public class DoctorAfterServicePA extends FragmentStatePagerAdapter {
    private String id;
    private EditForumFragment afterServiceForumFragment;
    private ViewForumFragment viewForumFragment;

    public DoctorAfterServicePA(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0: {
                viewForumFragment = ViewForumFragment.newInstance(id, AfterService.TYPE.PATIENT);
                return viewForumFragment;
            }
            case 1: {
                afterServiceForumFragment = EditForumFragment.newInstance(id, AfterService.TYPE.DOCTOR);
                return afterServiceForumFragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (!Settings.isDoctor()) {
            if (position == 0) {
                return "我的问卷";
            } else {
                return "医生建议";
            }
        } else {
            if (position == 0) {
                return "患者问卷";
            } else {
                return "病历记录";
            }
        }
    }

    public void saveAnswer() {
        if (afterServiceForumFragment != null) {
            afterServiceForumFragment.saveAnswer();
        }
    }

    public void handleImageResult(final int requestCode, int resultCode, Intent data) {
        if (afterServiceForumFragment != null) {
            afterServiceForumFragment.handleImageResult(requestCode, resultCode, data);
        }
    }

    public List<Prescription> getPrescriptions() {
        BaseAdapter adapter = viewForumFragment.getAdapter();
        for (int i = 0; i < adapter.size(); i++) {
            LayoutId layoutId = (LayoutId) adapter.get(i);
            if (layoutId.getItemLayoutId() == R.layout.item_answer) {
                Answer otherAnswer = (Answer) layoutId;
                if (otherAnswer.isDrugInit) {
                    return otherAnswer.getPrescriptions();
                }
            }
        }
        return new ArrayList<>();
    }
}
