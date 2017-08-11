package com.doctor.sun.ui.activity.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityAddressListBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Address;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.AddressAdapter;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 17/4/26.
 */

public class AddressManagerActivity extends BaseFragmentActivity2 {

    private PActivityAddressListBinding binding;
    private MyAdapter myAdapter;
    private ProfileModule api = Api.of(ProfileModule.class);
    private Intent getData;
    List<Address> list;

    public static Intent makeIntent(Context context, String position) {
        Intent i = new Intent(context, AddressManagerActivity.class);
        i.putExtra(Constants.DRUG_ID, position);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("delete");
        intentFilter.addAction("addAddress");
        intentFilter.addAction("mockAddress");
        intentFilter.addAction("unMockAddress");
        intentFilter.addAction("updateSuccess");
        getData = getIntent();
        registerReceiver(receiver, intentFilter);
        initData();

        initListener();
    }

    public void initListener() {
        binding.tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdd = SingleFragmentActivity.intentFor(AddressManagerActivity.this,
                        "添加新地址", AddressAddFragment.uploadDrug(getDrugId()));
                startActivity(toAdd);
            }
        });
    }

    public void initData() {
        list = new ArrayList<>();
        LoadingHelper.showMaterLoading(AddressManagerActivity.this, "正在加载...");
        api.getAddressList().enqueue(new SimpleCallback<List<Address>>() {
            @Override
            protected void handleResponse(List<Address> response) {
                LoadingHelper.hideMaterLoading();
                list = response;
                myAdapter = new MyAdapter(list);
                if (response.isEmpty()) {
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
                binding.rvList.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<ApiDTO<List<Address>>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_address_list);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("addAddress")) {
                initData();
            } else if (intent.getAction().equals("delete")) {
                initData();
            } else if (intent.getAction().equals("mockAddress")) {
                list.get(intent.getIntExtra(Constants.RECEIVER, 0)).setDefaults("1");
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equals("unMockAddress")) {
                list.get(intent.getIntExtra(Constants.RECEIVER, 0)).setDefaults("0");
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            }else if (intent.getAction().equals("updateSuccess")){
                finish();
            }
        }
    };

    public class MyAdapter extends BaseAdapter {

        List<Address> list;

        public MyAdapter(List<Address> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ViewHolder holder;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddressManagerActivity.this).inflate(R.layout.p_item_addresslist, null);
                holder.item_address_mock = (CheckBox) convertView.findViewById(R.id.item_address_mock);
                holder.ll_address = (LinearLayout) convertView.findViewById(R.id.ll_address);
                holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
                holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
                holder.tv_edit = (TextView) convertView.findViewById(R.id.tv_edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Address address = list.get(position);
            holder.tv_address.setText(address.getProvince() + " " + address.getCity() + " " + address.getArea() + " " + address.getAddress());
            holder.tv_name.setText(address.getTo());
            holder.tv_phone.setText(address.getPhone());
            if (!TextUtils.isEmpty(address.getRemark())) {
                holder.tv_remark.setText("备注信息：" + address.getRemark());
            } else {
                holder.tv_remark.setVisibility(View.GONE);
            }
            if (address.getDefaults().equals("1")) {
                holder.item_address_mock.setChecked(true);
            } else {
                holder.item_address_mock.setChecked(false);
            }
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAddress(v.getContext(), address.getId());
                }
            });
            holder.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAddress(v.getContext(), address);
                }
            });
            holder.item_address_mock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMockAddress(v.getContext(), address, position);
                }
            });
            holder.ll_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(getDrugId())) {
                        showUpdateAddress(v.getContext(), address);
                    }
                    if (null == getData.getStringExtra(Constants.MOCK)) {
                        return;
                    }
                    showUpdateAddress(v.getContext(), address);
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout ll_address;
            TextView tv_name, tv_phone, tv_address, tv_remark;
            CheckBox item_address_mock;
            TextView tv_edit, tv_delete;
        }
    }

    public void showUpdateAddress(final Context context, final Address address) {
        if (TextUtils.isEmpty(getDrugId())) {
            return;
        }
        api.updateOrderAddress(address.getProvince() + "-" + address.getCity() + "-" + address.getArea() + "-" + address.getAddress(),
                getDrugId(), address.getPhone(), address.getRemark(), address.getTo())
                .enqueue(new SimpleCallback<Address>() {
                    @Override
                    protected void handleResponse(Address response) {
                        Intent intent = new Intent();
                        intent.setAction("updateSuccess");
                        context.sendBroadcast(intent);
                        finish();
                    }
                });

    }

    public void updateMockAddress(final Context context, Address address, final int position) {
        if (address.getDefaults().equals("1")) {
            LoadingHelper.showMaterLoading(context, "正在加载...");
            api.updateMockAddress(address.getId() + "", "0").enqueue(new Callback<ApiDTO>() {
                @Override
                public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                    LoadingHelper.hideMaterLoading();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setDefaults("0");
                    }
                    Intent intent = new Intent();
                    intent.setAction("unMockAddress");
                    intent.putExtra(Constants.RECEIVER, position);
                    context.sendBroadcast(intent);
                }

                @Override
                public void onFailure(Call<ApiDTO> call, Throwable t) {
                    LoadingHelper.hideMaterLoading();
                }
            });

        } else {
            LoadingHelper.showMaterLoading(context, "正在加载...");
            api.updateMockAddress(address.getId() + "", "1").enqueue(new Callback<ApiDTO>() {
                @Override
                public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                    LoadingHelper.hideMaterLoading();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setDefaults("0");
                    }
                    Intent intent = new Intent();
                    intent.setAction("mockAddress");
                    intent.putExtra(Constants.RECEIVER, position);
                    context.sendBroadcast(intent);
                }

                @Override
                public void onFailure(Call<ApiDTO> call, Throwable t) {
                    LoadingHelper.hideMaterLoading();
                }
            });
        }
    }


    public static void updateAddress(Context context, Address address) {
        Intent toUpdate = SingleFragmentActivity.intentFor(context, "修改收货地址", AddressAddFragment.update(address));
        context.startActivity(toUpdate);
    }

    public void deleteAddress(final Context context, final int addressId) {
        TwoChoiceDialog.show(context, "确定删除该地址吗?", "取消", "确定", new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(final MaterialDialog dialog) {
                LoadingHelper.showMaterLoading(context, "正在删除...");
                api.deleteAddress(addressId).enqueue(new Callback<ApiDTO<String>>() {
                    @Override
                    public void onResponse(Call<ApiDTO<String>> call, Response<ApiDTO<String>> response) {
                        if (response.body().getStatus().equals("200")) {
                            Intent intent = new Intent();
                            intent.setAction("delete");
                            context.sendBroadcast(intent);
                            LoadingHelper.hideMaterLoading();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                        LoadingHelper.hideMaterLoading();
                        dialog.dismiss();
                    }
                });

            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });

    }

    public String getDrugId() {
        return getIntent().getStringExtra(Constants.DRUG_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int getMidTitle() {
        return R.string.address_manager;
    }
}
