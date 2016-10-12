package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.MainTabChangedEvent;

import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;
import io.realm.RealmChangeListener;


/**
 * Created by rick on 10/15/15.
 */
public class FooterViewModel extends BaseObservable implements RealmChangeListener<Realm> {
    public static final String TAG = FooterViewModel.class.getSimpleName();

    public int id;

    private static FooterViewModel instance;

    public static FooterViewModel getInstance(int position) {
        if (instance == null) {
            instance = new FooterViewModel(position);
        } else {
            instance.id = position;
        }
        return instance;
    }

    public FooterViewModel(int position) {
        this.id = position;
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
                        EventHub.post(new MainTabChangedEvent(0));
                        break;
                    }
                    case R.id.tab_two: {
                        EventHub.post(new MainTabChangedEvent(1));
                        break;
                    }
                    case R.id.lly_message: {
                        EventHub.post(new MainTabChangedEvent(1));
                        break;
                    }
                    case R.id.tab_three: {
                        EventHub.post(new MainTabChangedEvent(2));
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

}