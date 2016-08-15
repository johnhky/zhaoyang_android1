package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.MedicineStore;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.Try;
import com.doctor.sun.vo.ItemConsulting;
import com.doctor.sun.vo.ItemLoadMore;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 12/18/15.
 */
public class ConsultingFragment2 extends SortedListFragment {
    public static final String TAG = ConsultingFragment2.class.getSimpleName();
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private HashMap<String, Appointment> appointments = new HashMap<>();
    private Observer<List<RecentContact>> observer;
    private ArrayList<String> keys = new ArrayList<>();
    private int page = 1;
    private HashMap<String, RecentContact> tids;
    private SystemMsg systemMsg;
    private MedicineStore medicineStore;

    public ConsultingFragment2() {
    }


    @Override
    public void onResume() {
        super.onResume();
        getSystemMsg();
        getMedicineStore();
        registerRecentContactObserver();
    }

    public MedicineStore getMedicineStore() {
        if (medicineStore == null) {
            medicineStore = new MedicineStore();
        }
        medicineStore.notifyChange();
        medicineStore.registerRealmChanged();
        return medicineStore;
    }

    public SystemMsg getSystemMsg() {
        if (systemMsg == null) {
            systemMsg = new SystemMsg();
        }
        systemMsg.notifyChange();
        systemMsg.registerMsgsChangedListener();
        return systemMsg;
    }

    private void registerRecentContactObserver() {
        createRecentContactObserver();
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observer, true);
    }

    private Observer<List<RecentContact>> createRecentContactObserver() {
        if (observer == null) {
            observer = new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(final List<RecentContact> recentContacts) {
                    if (recentContacts == null) return;

                    final HashMap<String, RecentContact> tids = getTids(recentContacts);
                    Tasks.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (String s : tids.keySet()) {
                                Appointment appointment = appointments.get(s);
                                if (appointment != null) {
                                    String body = appointment.getHandler().lastMsg().getBody();
                                    if (Constants.refreshMsg.contains(body)) {
                                        pullAppointment(s, tids.get(s));
                                    } else {
                                        ItemConsulting itemConsulting = new ItemConsulting(tids.get(s), appointment);
                                        getAdapter().update(itemConsulting);
                                    }
                                } else {
                                    pullAppointment(s, tids.get(s));
                                }
                            }
                        }
                    }, 200);
                }
            };
        }
        return observer;
    }

    public void pullAppointment(String s, final RecentContact recentContact) {
        api.appointmentInTid("[" + s + "]", "1").enqueue(new SimpleCallback<PageDTO<Appointment>>() {
            @Override
            protected void handleResponse(final PageDTO<Appointment> response) {
                for (Appointment appointment : response.getData()) {
                    String tid = String.valueOf(appointment.getTid());
                    ItemConsulting itemConsulting = new ItemConsulting(recentContact, appointment);
                    appointments.put(tid, appointment);
                    getAdapter().insert(itemConsulting);
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadMore();
        insertHeader();
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 100);
    }

    public void insertHeader() {
        getAdapter().insert(getSystemMsg());
        if (!Settings.isDoctor()) {
            getAdapter().insert(getMedicineStore());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadMore() {
        super.loadMore();
        if (keys == null || keys.isEmpty()) {
            MsgService service = NIMClient.getService(MsgService.class);
            service.queryRecentContacts()
                    .setCallback(new RecentContactCallback());
        } else {
            api.appointmentInTid(JacksonUtils.toJson(keys), page + "").enqueue(getCallback(tids));
        }
    }


    @Override
    public void onRefresh() {
        page = 1;
        getAdapter().clear();
        insertHeader();
        keys.clear();
        super.onRefresh();
    }

    private class RecentContactCallback extends RequestCallbackWrapper<List<RecentContact>> {
        @Override
        public void onResult(int code, List<RecentContact> recents, Throwable e) {
            // recents参数即为最近联系人列表（最近会话列表）
            if (recents == null) return;

            tids = getTids(recents);
            api.appointmentInTid(JacksonUtils.toJson(keys), page + "").enqueue(getCallback(tids));
        }

        @Override
        public void onFailed(int i) {
            super.onFailed(i);
            IMManager.getInstance().login();
            hideRefreshing();
        }

        @Override
        public void onException(Throwable throwable) {
            super.onException(throwable);
            IMManager.getInstance().login();
            hideRefreshing();
        }
    }

    public void hideRefreshing() {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefresh.setRefreshing(false);
            }
        }, 500);
    }

    @NonNull
    public SimpleCallback<PageDTO<Appointment>> getCallback(final HashMap<String, RecentContact> tids) {
        return new SimpleCallback<PageDTO<Appointment>>() {
            @Override
            protected void handleResponse(PageDTO<Appointment> response) {
                page += 1;
                for (Appointment appointment : response.getData()) {
                    String tid = String.valueOf(appointment.getTid());
                    appointments.put(tid, appointment);
                    ItemConsulting itemConsulting = new ItemConsulting(tids.get(tid), appointment);
                    getAdapter().insert(itemConsulting);
                }
                int to = response.getTo();
                int total = response.getTotal();
                int perPage = response.getPerPage();
                boolean finished = to >= total || (page - 1) * perPage >= total;
                if (!finished) {
                    insertLoadMore();
                } else {
                    getAdapter().removeItem(new ItemLoadMore());
                }
                isLoading = false;
                showShowCase();

                hideRefreshing();
                if (getAdapter().getItemCount() <= getHeaderItemCount()) {
                    binding.emptyIndicator.setText("您当前没有进行中的聊天");
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
            }
        };
    }

    private int getHeaderItemCount() {
        if (Settings.isDoctor()) {
            return 1;
        }
        return 2;
    }

    private void insertLoadMore() {
        final ItemLoadMore item = new ItemLoadMore();
        item.setLoadMoreListener(new Try() {
            @Override
            public void success() {
                loadMore();
            }

            @Override
            public void fail() {
            }
        });
        getAdapter().insert(item);
    }


    private void showShowCase() {
        if (ShowCaseUtil.isShow(TAG)) {
            return;
        }
        if (!Settings.isDoctor()) {
            View systemMsg = binding.recyclerView.findViewById(R.id.system_msg);
            View medicineStore = binding.recyclerView.findViewById(R.id.medicine_store);

            ShowCaseUtil.showCase(systemMsg, "昭阳医生系统会向您推送所有的系统消息", TAG, 2, 0, true);
            ShowCaseUtil.showCase(medicineStore, "昭阳医生系统会向您推送所有的系统消息", TAG, 2, 1, true);
        }
    }

    @NonNull
    private HashMap<String, RecentContact> getTids(List<RecentContact> recents) {
        HashMap<String, RecentContact> tids = new HashMap<>();
        for (RecentContact recent : recents) {
            String contactId = recent.getContactId();
            if (recent.getSessionType() == SessionTypeEnum.Team) {
                tids.put(contactId, recent);
                keys.add(contactId);
            }
        }
        return tids;
    }
}
