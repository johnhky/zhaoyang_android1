package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.MedicineStore;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.adapter.ConsultingAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.JacksonUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.util.Tasks;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by rick on 12/18/15.
 */
public class ConsultingFragment extends RefreshListFragment {
    public static final String TAG = ConsultingFragment.class.getSimpleName();
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private PageCallback<Appointment> callback;
    private SparseArray<Appointment> map = new SparseArray<>();
    private ArrayList<LayoutId> headers;

    public ConsultingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMore();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerRecentContactObserver();
    }

    private void registerRecentContactObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(final List<RecentContact> recentContacts) {
                int padding = getItemPadding();
                final int finalPosition = padding;
                Tasks.runOnUiThread(new InsertAppointmentRunnable(recentContacts, finalPosition), 300);
            }
        }, true);
    }

    private int getItemPadding() {
        int padding = 2;
        if (AppContext.isDoctor()) {
            padding = 1;
        }
        return padding;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initListener();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        ConsultingAdapter adapter = new ConsultingAdapter(getContext(), realm);
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (userType == AuthModule.PATIENT_TYPE) {
            adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_consulting);
            initHeader(AuthModule.PATIENT_TYPE);
        } else {
            adapter.mapLayout(R.layout.item_appointment, R.layout.item_consulting);
            initHeader(AuthModule.DOCTOR_TYPE);
        }
        adapter.addAll(headers);
        return adapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadMore() {
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (headers == null) {
            initHeader(userType);
        }

        callback = new AppointmentPageCallback();
        MsgService service = NIMClient.getService(MsgService.class);
        service.queryRecentContacts()
                .setCallback(new RecentContactCallback());
    }

    private void initHeader(int userType) {
        headers = new ArrayList<>();
        headers.add(new SystemMsg());
        if (userType == AuthModule.PATIENT_TYPE) {
            headers.add(new MedicineStore());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getAdapter().notifyItemChanged(0);
    }

    private void initListener() {
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefresh.setRefreshing(true);
                callback.setRefresh();
                loadMore();
            }
        };
        binding.swipeRefresh.setOnRefreshListener(listener);
    }


    public Appointment getAppointmentByTid(String tid) {
        return getAppointmentByTid(Integer.valueOf(tid));
    }

    public Appointment getAppointmentByTid(int tid) {
        Appointment appointment = map.get(tid);
        if (isStartedMsg(tid)) {
            int i = removeAppointment(appointment);
            getAdapter().notifyItemRemoved(i);
            pullAppointment(tid);
            return null;
        } else if (appointment != null) {
            return appointment;
        } else {
            pullAppointment(tid);
            return null;
        }
    }

    public boolean isStartedMsg(int tid) {
        if (realm == null || realm.isClosed()) return false;

        RealmResults<TextMsg> time = realm.where(TextMsg.class).equalTo("sessionId", String.valueOf(tid)).findAllSorted("time", Sort.ASCENDING);

        return time.last().getBody().equals("就诊开始");
    }

    private void pullAppointment(int tid) {
        api.appointmentInTid(String.valueOf(tid), "1").enqueue(new InsertAppointmentCallback());
    }

    private void cacheAppointment(List<Appointment> data) {
        for (Appointment appointment : data) {
            map.put(appointment.getTid(), appointment);
        }
    }

    private class InsertAppointmentRunnable implements Runnable {
        private final List<RecentContact> recentContacts;
        private final int finalPosition;

        public InsertAppointmentRunnable(List<RecentContact> recentContacts, int finalPosition) {
            this.recentContacts = recentContacts;
            this.finalPosition = finalPosition;
        }

        @Override
        public void run() {
            for (int i = 0; i < recentContacts.size(); i++) {
                RecentContact recentContact = recentContacts.get(i);
                String contactId = recentContact.getContactId();
                if (recentContact.getSessionType() == SessionTypeEnum.Team) {
                    getAndInsertAppointment(contactId);
                }
            }
        }

        private void getAndInsertAppointment(String contactId) {
            Appointment appointmentByTid = getAppointmentByTid(contactId);
            if (appointmentByTid != null) {
                int oldPosition = removeAppointment(appointmentByTid);
                getAdapter().add(finalPosition, appointmentByTid);
                if (oldPosition != finalPosition) {
                    getAdapter().notifyItemMoved(oldPosition, finalPosition);
                    getAdapter().notifyItemChanged(finalPosition);
                } else {
                    getAdapter().notifyItemChanged(oldPosition);
                }
            }
        }
    }

    private int removeAppointment(Appointment appointmentByTid) {
        int oldPosition = getAdapter().indexOf(appointmentByTid);
        getAdapter().remove(oldPosition);
        map.remove(appointmentByTid.getTid());
        return oldPosition;
    }

    private class RecentContactCallback extends RequestCallbackWrapper<List<RecentContact>> {
        @Override
        public void onResult(int code, List<RecentContact> recents, Throwable e) {
            // recents参数即为最近联系人列表（最近会话列表）
            if (recents == null) return;


            ArrayList<String> tids = getTids(recents);
            api.appointmentInTid(JacksonUtils.toJson(tids), callback.getPage()).enqueue(callback);
        }

        @Override
        public void onFailed(int i) {
            super.onFailed(i);
            IMManager.getInstance().login();
            getAdapter().onFinishLoadMore(true);
        }

        @Override
        public void onException(Throwable throwable) {
            super.onException(throwable);
            IMManager.getInstance().login();
            getAdapter().onFinishLoadMore(true);
        }
    }

    @NonNull
    private ArrayList<String> getTids(List<RecentContact> recents) {
        ArrayList<String> tids = new ArrayList<>();
        for (RecentContact recent : recents) {
            String contactId = recent.getContactId();
            if (recent.getSessionType() == SessionTypeEnum.Team) {
                tids.add(contactId);
            }
        }
        return tids;
    }


    private class AppointmentPageCallback extends PageCallback<Appointment> {
        public AppointmentPageCallback() {
            super(ConsultingFragment.this.getAdapter());
        }

        @Override
        public void onInitHeader() {
            super.onInitHeader();
            getAdapter().addAll(headers);
        }

        @Override
        protected void handleResponse(PageDTO<Appointment> response) {
            ArrayList<Appointment> data = (ArrayList<Appointment>) response.getData();
            for (Appointment appointment : data) {
                map.put(appointment.getTid(), appointment);
            }
            super.handleResponse(response);
        }

        @Override
        public void onFinishRefresh() {
            super.onFinishRefresh();
            binding.swipeRefresh.setRefreshing(false);
        }
    }

    private class InsertAppointmentCallback extends SimpleCallback<PageDTO<Appointment>> {
        @Override
        protected void handleResponse(PageDTO<Appointment> response) {
            List<Appointment> data = response.getData();
            if (data != null) {
                int padding = getItemPadding();
                getAdapter().addAll(padding, data);
                getAdapter().notifyItemRangeInserted(padding, data.size());
                cacheAppointment(data);
            }
        }
    }
}
