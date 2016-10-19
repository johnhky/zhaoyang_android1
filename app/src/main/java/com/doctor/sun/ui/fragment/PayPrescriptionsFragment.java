package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.entity.MedicineInfo;
import com.doctor.sun.model.PayPrescriptionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.PayEventHandler;

import java.util.List;

/**
 * Created by rick on 14/9/2016.
 * 寄药订单支付界面
 */

public class PayPrescriptionsFragment extends SortedListFragment {
    public static final String TAG = PayPrescriptionsFragment.class.getSimpleName();

    private PayPrescriptionsModel model;
    private Address address;
    private Doctor doctor;
    private MedicineInfo medicineInfo;
    private boolean hasPay;
    private PayEventHandler payEventHandler;

    public static Bundle getArgs(Drug drug) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        Address address = new Address();
        address.setName(drug.getTo());
        address.setPhone(drug.getPhone());
        address.setAddress(drug.getAddress());

        Doctor doctor = drug.getDoctor();

        MedicineInfo medicineInfo = new MedicineInfo();
        medicineInfo.setOrderId(String.valueOf(drug.getId()));
        // 药品信息分行显示
        StringBuilder sb = new StringBuilder();
        for (String medicine : drug.getDrug()) {
            sb.append(medicine);
            sb.append("\n");
        }
        // 删掉最后一个换行符
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        medicineInfo.setMedicine(sb.toString());
        medicineInfo.setMedicinePrice(drug.getMoney());

        bundle.putParcelable(Constants.ADDRESS, address);
        bundle.putParcelable(Constants.DOCTOR_INFO, doctor);
        bundle.putParcelable(Constants.MEDICINE_INFO, medicineInfo);
        bundle.putBoolean(Constants.HAS_PAY, drug.getHasPay() != 0);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new PayPrescriptionsModel();
        address = getArguments().getParcelable(Constants.ADDRESS);
        doctor = getArguments().getParcelable(Constants.DOCTOR_INFO);
        medicineInfo = getArguments().getParcelable(Constants.MEDICINE_INFO);
        hasPay = getArguments().getBoolean(Constants.HAS_PAY);
        payEventHandler = PayEventHandler.register();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();

        List<SortedItem> sortedItems = model.parseData(address, doctor, medicineInfo, hasPay);
        binding.swipeRefresh.setRefreshing(false);
        getAdapter().insertAll(sortedItems);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PayEventHandler.unregister(payEventHandler);
    }
}
