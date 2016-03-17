package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.entity.VoipAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.im.Messenger;

import io.realm.Realm;
import io.realm.RealmChangeListener;


/**
 * Created by rick on 10/15/15.
 */
public class FooterViewModel extends BaseObservable {
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
        final VoipAccount accountDTO = Messenger.getVoipAccount();
        if (accountDTO == null) return;
        final String voipAccount = accountDTO.getVoipAccount();
        long unReadMsg = realm.where(TextMsg.class).equalTo("haveRead", false).count();
        setCount((int) unReadMsg);

        realm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
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
}