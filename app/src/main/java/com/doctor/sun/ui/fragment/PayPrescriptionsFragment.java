package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.DoctorInfo;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.entity.MedicineInfo;
import com.doctor.sun.model.PayPrescriptionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.List;

/**
 * Created by rick on 14/9/2016.
 * 寄药订单支付界面
 */

public class PayPrescriptionsFragment extends SortedListFragment {
    public static final String TAG = PayPrescriptionsFragment.class.getSimpleName();

    private PayPrescriptionsModel model;
    private Address address;
    private DoctorInfo doctorInfo;
    private MedicineInfo medicineInfo;

    public static Bundle getArgs(Drug drug) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        // 测试用例，实际上应该从网络获取数据
        Address address = new Address();
        address.setName(drug.getTo());
        address.setPhone(drug.getPhone());
        address.setAddress(drug.getAddress());

        Doctor doctor = drug.getDoctor();
        DoctorInfo doctorInfo = new DoctorInfo();
        doctorInfo.setName(doctor.getName());
        doctorInfo.setHospital(doctor.getHospitalName());
        doctorInfo.setLevel(doctor.getLevel());
        doctorInfo.setSpecialist(doctor.getSpecialist());
        doctorInfo.setTitle(doctor.getTitle());

        MedicineInfo medicineInfo = new MedicineInfo();
        medicineInfo.setOrderId(String.valueOf(drug.getId()));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("药");
        stringBuffer.append("\n");
        for (String medicine : drug.getDrug()) {
            stringBuffer.append(medicine);
            stringBuffer.append("\n");
        }
        // 删掉最后一个换行符
        if (stringBuffer.length() > 0) {
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        medicineInfo.setMedicine(stringBuffer.toString());
        medicineInfo.setMedicinePrice(drug.getMoney());

        bundle.putParcelable(Constants.ADDRESS, address);
        bundle.putParcelable(Constants.DOCTOR_INFO, doctorInfo);
        bundle.putParcelable(Constants.MEDICINE_INFO, medicineInfo);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new PayPrescriptionsModel();
        address = getArguments().getParcelable(Constants.ADDRESS);
        doctorInfo = getArguments().getParcelable(Constants.DOCTOR_INFO);
        medicineInfo = getArguments().getParcelable(Constants.MEDICINE_INFO);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<SortedItem> sortedItems = model.parseData(address, doctorInfo, medicineInfo);
        binding.swipeRefresh.setRefreshing(false);
        getAdapter().insertAll(sortedItems);
    }
}
