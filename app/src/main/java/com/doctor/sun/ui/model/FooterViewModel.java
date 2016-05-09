package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.im.Messenger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;


/**
 * Created by rick on 10/15/15.
 */
public class FooterViewModel extends BaseObservable {
    public static final String TAG = FooterViewModel.class.getSimpleName();
    private FooterView mView;

    public int id;
    private String count;
    private Realm realm;

    private static FooterViewModel instance;

    public static FooterViewModel getInstance(FooterView mView, final Realm realm, int position) {
        if (instance == null) {
            instance = new FooterViewModel(mView, realm, position);
        } else {
            instance.mView = null;
            instance.realm = null;
            instance.mView = mView;
            instance.realm = realm;
            instance.id = position;
        }
        return instance;
    }

    public FooterViewModel(FooterView mView, final Realm realm, int position) {
        this.mView = mView;
        this.id = position;
        this.realm = realm;
        final ImAccount accountDTO = Messenger.getVoipAccount();
        if (accountDTO == null) return;
        final String voipAccount = accountDTO.getVoipAccount();
        RealmQuery<TextMsg> haveRead = realm.where(TextMsg.class).equalTo("haveRead", false);
        long unReadMsg = haveRead.count();
        setCount((int) unReadMsg);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            /**
             * Called when a transaction is committed.
             *
             * @param realm
             */
            @Override
            public void onChange(Realm realm) {
                long unReadMsg = realm.where(TextMsg.class).equalTo("haveRead", false).count();
                setCount((int) unReadMsg);
            }
        });
    }

    public int haveUnreadMsg() {
        long unReadMsg = realm.where(TextMsg.class).equalTo("haveRead", false).count();
        if (unReadMsg != 0)
            return View.VISIBLE;
        else
            return View.GONE;
    }

    public int getId() {
        return id;
    }

    @Bindable
    public String getCount() {
        return count;
    }

    public FooterViewModel setCount(long count) {
        this.count = String.valueOf(count);
        notifyChange();
        return this;
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