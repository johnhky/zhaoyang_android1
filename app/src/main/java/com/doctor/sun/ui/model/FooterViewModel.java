package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.im.Messenger;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Created by rick on 10/15/15.
 */
public class FooterViewModel extends BaseObservable implements RealmChangeListener<Realm>{
    public static final String TAG = FooterViewModel.class.getSimpleName();
    private FooterView mView;

    public int id;

    private static FooterViewModel instance;

    public static FooterViewModel getInstance(FooterView mView, int position) {
        if (instance == null) {
            instance = new FooterViewModel(mView, position);
        } else {
            instance.mView = null;
            instance.mView = mView;
            instance.id = position;
        }
        return instance;
    }

    public FooterViewModel(FooterView mView, int position) {
        this.mView = mView;
        this.id = position;
        final ImAccount accountDTO = Messenger.getVoipAccount();
        if (accountDTO == null) return;
    }

    @NonNull
    public long getUnReadMsgCount() {
        return getUnReadCount(Realm.getDefaultInstance());
    }


    private long getUnReadCount(Realm realm) {
        return realm.where(TextMsg.class).equalTo("haveRead", false).count();
    }

    public int getId() {
        return id;
    }

    public View.OnClickListener onFooterClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (id == i) {
                    return;
                }
                switch (i) {
                    case R.id.tab_one: {
                        mView.gotoTabOne();
                        break;
                    }
                    case R.id.tab_two: {
                        mView.gotoTabTwo();
                        break;
                    }
                    case R.id.lly_message: {
                        mView.gotoTabTwo();
                        break;
                    }
                    case R.id.tab_three: {
                        mView.gotoTabThree();
                        break;
                    }
                }
            }
        };
    }
    @Override
    public void onChange(Realm element) {
        notifyChange();
    }


    public interface FooterView {

        void gotoTabOne();

        void gotoTabTwo();

        void gotoTabThree();

    }

    public String getShowCaseContent() {
        if (AppContext.isDoctor()) {
            return "您可以在这里与患者通过文字信息或者电话进行沟通";
        } else {
            return "您可以在这里与医生通过文字信息或者电话进行沟通";
        }
    }

    public String getShowCaseId() {
        if (AppContext.isDoctor()) {
            return "doctorMain";
        } else {
            return "main";
        }
    }

    public int getShowPosition() {
        if (AppContext.isDoctor()) {
            return 1;
        } else {
            return 3;
        }
    }

    public int getShowCaseSize() {
        if (AppContext.isDoctor()) {
            return 2;
        } else {
            return 4;
        }
    }
}