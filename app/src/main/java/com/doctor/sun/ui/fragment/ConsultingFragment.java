package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.MedicineHelper;
import com.doctor.sun.entity.SystemTip;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.adapter.ConsultingAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.util.JacksonUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.HashSet;
import java.util.List;

import io.ganguo.library.Config;
import io.realm.RealmChangeListener;

/**
 * Created by rick on 12/18/15.
 */
public class ConsultingFragment extends RefreshListFragment {
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private RealmChangeListener listener;
    private PageCallback<Appointment> callback;

    public ConsultingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = new RealmChangeListener() {
            @Override
            public void onChange() {
                getAdapter().notifyDataSetChanged();
            }
        };
        realm.addChangeListener(listener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initListener();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (!realm.isClosed()) {
            realm.removeChangeListener(listener);
        }
        super.onDestroy();
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        ConsultingAdapter adapter = new ConsultingAdapter(getContext(), realm);
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (userType == AuthModule.PATIENT_TYPE) {
            adapter.add(new SystemTip());
            adapter.add(new MedicineHelper());
            adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_consulting);
        } else {
            adapter.add(new SystemTip());
            adapter.mapLayout(R.layout.item_appointment, R.layout.item_consulting);
        }
        return adapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadMore() {
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (userType == AuthModule.PATIENT_TYPE) {
            callback = new PageCallback<Appointment>(getAdapter()) {
                @Override
                public void onInitHeader() {
                    super.onInitHeader();
                    getAdapter().add(new SystemTip());
                    getAdapter().add(new MedicineHelper());
                }

                @Override
                public void onFinishRefresh() {
                    super.onFinishRefresh();
                    binding.swipeRefresh.setRefreshing(false);
                }
            };
            NIMClient.getService(MsgService.class).queryRecentContacts()
                    .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                        @Override
                        public void onResult(int code, List<RecentContact> recents, Throwable e) {
                            // recents参数即为最近联系人列表（最近会话列表）
                            if (recents == null) return;


                            HashSet<String> tids = new HashSet<String>();
                            for (RecentContact recent : recents) {
                                String contactId = recent.getContactId();
                                if (recent.getSessionType() == SessionTypeEnum.Team) {
                                    tids.add(contactId);
                                }
                            }
                            api.appointmentInTid(JacksonUtils.toJson(tids), callback.getPage()).enqueue(callback);
                        }

                        @Override
                        public void onFailed(int i) {
                            super.onFailed(i);
                            com.doctor.sun.im.Messenger.getInstance().login();
                            getAdapter().onFinishLoadMore(true);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            com.doctor.sun.im.Messenger.getInstance().login();
                            getAdapter().onFinishLoadMore(true);
                        }
                    });
        } else {
            callback = new PageCallback<Appointment>(getAdapter()) {
                @Override
                public void onInitHeader() {
                    super.onInitHeader();
                    getAdapter().add(new SystemTip());
                }

                @Override
                public void onFinishRefresh() {
                    super.onFinishRefresh();
                    binding.swipeRefresh.setRefreshing(false);
                }
            };
            NIMClient.getService(MsgService.class).queryRecentContacts()
                    .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                        @Override
                        public void onResult(int code, List<RecentContact> recents, Throwable e) {
                            // recents参数即为最近联系人列表（最近会话列表）
                            if (recents == null) return;

                            HashSet<String> tids = new HashSet<String>();
                            for (RecentContact recent : recents) {
                                String contactId = recent.getContactId();
                                if (recent.getSessionType() == SessionTypeEnum.Team) {
                                    tids.add(contactId);
                                }
                            }
                            api.appointmentInTid(JacksonUtils.toJson(tids), callback.getPage()).enqueue(callback);
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getAdapter().notifyItemChanged(0);
    }

    private void initListener() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefresh.setRefreshing(true);
                callback.setRefresh();
                loadMore();
            }
        });
    }
}
