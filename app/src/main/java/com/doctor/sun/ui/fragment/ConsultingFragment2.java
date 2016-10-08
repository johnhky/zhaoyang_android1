package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.MedicineStore;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
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
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.Tasks;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by rick on 12/18/15.
 */
public class ConsultingFragment2 extends SortedListFragment {
    public static final String TAG = ConsultingFragment2.class.getSimpleName();
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private ArrayList<String> keys = new ArrayList<>();
    private int page = 1;
    private HashMap<String, RecentContact> tids;
    private SystemMsg systemMsg;
    private MedicineStore medicineStore;

    private String lastRefreshMsgId = "";
    private RealmResults<TextMsg> unReadMsg;
    private RealmChangeListener<RealmResults<TextMsg>> listener;

    public ConsultingFragment2() {
    }


    @Override
    public void onResume() {
        super.onResume();
        getSystemMsg();
        getMedicineStore();
        registerRealmChangeListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegisterRealChangedListener();
    }

    private void unRegisterRealChangedListener() {
        if (unReadMsg != null) {
            unReadMsg.removeChangeListeners();
        }
    }

    private void registerRealmChangeListener() {
        createRealmChangeListener();
        if (unReadMsg != null) {
            unReadMsg.addChangeListener(listener);
        }
    }

    private void createRealmChangeListener() {
        if (listener == null) {
            listener = new RealmChangeListener<RealmResults<TextMsg>>() {
                @Override
                public void onChange(RealmResults<TextMsg> element) {
                    boolean empty = element.isEmpty();
                    boolean shouldRefresh;
                    if (!empty) {
                        TextMsg first = element.first();
                        if (first.getMsgId().equals(lastRefreshMsgId)) return;

                        shouldRefresh = TextMsgFactory.isRefreshMsg(first.getType());
                        String s = first.getSessionId();
                        if (s.startsWith("SYSTEM_MSG")) {
                            getAdapter().notifyItemChanged(0);
                        } else {
                            ItemConsulting appointment = (ItemConsulting) getAdapter().get(s);
                            lastRefreshMsgId = first.getMsgId();
                            if (appointment != null && !shouldRefresh) {
                                appointment.setTime(System.currentTimeMillis());
                                getAdapter().update(appointment);
                            } else {
                                pullAppointment(s, tids.get(s));
                            }
                        }
                    }
                }
            };
        }
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
            systemMsg.setItemLayoutId(R.layout.p_item_system_msg);
        }
        systemMsg.notifyChange();
        systemMsg.registerMsgsChangedListener();
        return systemMsg;
    }

    public void pullAppointment(String s, final RecentContact recentContact) {
        api.appointmentInTid("[" + s + "]", "1").enqueue(new SimpleCallback<PageDTO<Appointment>>() {
            @Override
            protected void handleResponse(final PageDTO<Appointment> response) {
                for (Appointment appointment : response.getData()) {
                    ItemConsulting itemConsulting = new ItemConsulting(recentContact.getTime(), appointment);
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
        queryUnreadMsg();
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 100);
    }

    public void queryUnreadMsg() {
        RealmQuery<TextMsg> query = Realm.getDefaultInstance().where(TextMsg.class)
                .equalTo("haveRead", false);
        unReadMsg = query.findAllSorted("time", Sort.DESCENDING);
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
                    ItemConsulting itemConsulting = new ItemConsulting(tids.get(tid).getTime(), appointment);
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
            ShowCaseUtil.showCase(medicineStore, "在这里联系小助手使用代寄药服务", TAG, 2, 1, true);
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
