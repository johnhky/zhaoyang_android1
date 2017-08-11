package com.doctor.sun.ui.activity.patient;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPayprescriptionBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Drug;
import com.doctor.sun.immutables.DrugOrderDetail;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.util.DialogUtils;
import com.doctor.sun.util.PayEventHandler;
import com.doctor.sun.util.PreferenceHelper;
import com.doctor.sun.util.WindowManagerUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import retrofit2.Call;

/**
 * Created by heky on 17/7/12.
 */

public class PayPrescriptionActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    DrugModule api = Api.of(DrugModule.class);
    private ProfileModule couponApi = Api.of(ProfileModule.class);
    ActivityPayprescriptionBinding binding;
    DrugOrderDetail detail;
    int selectedCoupon = -1;
    List<Coupon> coupons = new ArrayList<>();
    int id = 1;
    private ProfileModule apis = Api.of(ProfileModule.class);
    private PayEventHandler payEventHandler;
    public static Intent makeIntent(Context context, String drugId) {
        Intent i = new Intent();
        i.setClass(context, PayPrescriptionActivity.class);
        i.putExtra(Constants.DATA, drugId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PayPrescriptionActivity.this, R.layout.activity_payprescription);
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateSuccess");
        registerReceiver(receiver, filter);
        binding.ivBack.setOnClickListener(this);
        binding.rlUpdate.setOnClickListener(this);
        binding.tvAddAddress.setOnClickListener(this);
        binding.llSelectCoupon.setOnClickListener(this);
        binding.tvFeeDetail.setOnClickListener(this);
        binding.tvPay.setOnClickListener(this);
        binding.tvDrugHelper.setOnClickListener(this);
        payEventHandler = PayEventHandler.register(PayPrescriptionActivity.this);
        initData(0);
    }

    public void initData(final int position) {
        LoadingHelper.showMaterLoading(PayPrescriptionActivity.this,"正在加载...");
        api.drugDetail(getDrugId()).enqueue(new SimpleCallback<DrugOrderDetail>() {
            @Override
            protected void handleResponse(DrugOrderDetail response) {
                LoadingHelper.hideMaterLoading();
                detail = response;
                initData(response, position);
                loadCoupons(response);
            }

            @Override
            public void onFailure(Call<ApiDTO<DrugOrderDetail>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    public void initData(DrugOrderDetail data, int position) {
        binding.tvTitle.setText(data.getRecord_name() + "的寄药单");
        List<Drug.DrugEntity> lists = new ArrayList<>();
        for (Drug.DrugEntity s : data.getDrug_detail()) {
            lists.add(s);
        }
        for (int i = 0; i < data.getDrug_detail().size(); i++) {
            View view = LayoutInflater.from(PayPrescriptionActivity.this).inflate(R.layout.item_drugdetail, null);
            TextView tv_drugName = (TextView) view.findViewById(R.id.tv_drugName);
            TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
            tv_drugName.setText(data.getDrug_detail().get(i).drug + data.getDrug_detail().get(i).specification);
            tv_num.setText(data.getDrug_detail().get(i).drug_num);
            if (position == 0) {
                binding.llDrug.addView(view);
            }
        }
        binding.txtFee.setText("￥" + data.getDrug_money());
        /*binding.tvSaveFee.setText("省￥" + data.getSave_money());*/
        binding.tvNeedPay.setText(data.getNeed_pay() + "");
        if (data.getCharge().commission.isEmpty()) {
            View view = LayoutInflater.from(PayPrescriptionActivity.this).inflate(R.layout.item_drug_discount, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_discountTitle);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_discountContent);
            tv_title.setText("减免");
            tv_content.setText("免收服务费￥" + data.getService_fee());
            if (position == 0) {
                binding.llDiscount.addView(view);
            }

        }
        if (!data.getCharge().discount.isEmpty()) {
            for (int i = 0; i < data.getCharge().discount.size(); i++) {
                View view = LayoutInflater.from(PayPrescriptionActivity.this).inflate(R.layout.item_drug_discount, null);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_discountTitle);
                TextView tv_content = (TextView) view.findViewById(R.id.tv_discountContent);
                tv_title.setText("优惠");
                tv_content.setText(data.getCharge().discount.get(i));
                if (position==0){
                    binding.llDiscount.addView(view);
                }
            }
        }

        if (data.getHas_pay() == 1) {
            binding.tvPay.setText("已付款");
            binding.tvPay.setEnabled(false);
            binding.tvChange.setVisibility(View.GONE);
            binding.llSelectCoupon.setEnabled(false);
        }

        if (!TextUtils.isEmpty(data.getAddress())) {
            binding.tvAddAddress.setVisibility(View.GONE);
            binding.rlUpdate.setVisibility(View.VISIBLE);
            binding.tvName.setText(data.getTo());
            binding.tvPhone.setText(data.getPhone());
            binding.tvAddress.setText(data.getAddress());
            if (!TextUtils.isEmpty(data.getRemark())) {
                binding.tvRemark.setText("备注信息：" + data.getRemark());
            } else {
                binding.tvRemark.setVisibility(View.GONE);
            }

            if (data.getHas_pay() == 1) {
                binding.tvMark.setVisibility(View.VISIBLE);
                binding.rlUpdate.setEnabled(false);
            }
        } else {
            binding.tvAddAddress.setVisibility(View.VISIBLE);
            if (data.getHas_pay() == 1) {
                binding.tvMark.setVisibility(View.VISIBLE);
                binding.tvAddAddress.setEnabled(false);
            }
            binding.rlUpdate.setVisibility(View.GONE);
        }


    }

    private void loadCoupons(final DrugOrderDetail data) {
        couponApi.coupons(CouponType.CAN_USE_NOW, Coupon.Scope.DRUG_ORDER, data.getNeed_pay()).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    setCoupons(response);
                    if (selectedCoupon == -1) {
                        binding.tvSelectCoupon.setText(getText(data));
                    }

                } else {
                    binding.llSelectCoupon.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showChooseAddress() {
        Intent toIntent = AddressManagerActivity.makeIntent(PayPrescriptionActivity.this,getDrugId());
        startActivity(toIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_addAddress:
                apis.getAddressList().enqueue(new SimpleCallback<List<Address>>() {
                    @Override
                    protected void handleResponse(List<Address> response) {
                        if (response.size()>0){
                        showChooseAddress();
                        }else{
                            Intent toAdd = SingleFragmentActivity.intentFor(PayPrescriptionActivity.this,
                                    "添加新地址", AddressAddFragment.uploadDrug(getDrugId()));
                            startActivity(toAdd);
                        }
                    }
                });

                break;
            case R.id.rl_update:
                showChooseAddress();
                break;
            case R.id.ll_selectCoupon:
                selectCoupon();
                break;
            case R.id.tv_feeDetail:
                show(PayPrescriptionActivity.this, detail);
                break;
            case R.id.tv_pay:
                showPayDialog();
                break;
            case R.id.tv_drugHelper:
                Intent toMedicine = new Intent();
                toMedicine.setClass(PayPrescriptionActivity.this, MedicineStoreActivity.class);
                toMedicine.putExtra(Constants.DATA, 1);
                startActivity(toMedicine);
                break;
        }
    }

    public void show(Context context, DrugOrderDetail data) {
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.item_extrafee_detail, null);
        TextView tv_extraFee = (TextView) view.findViewById(R.id.tv_extraFee);
        LinearLayout ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
        TextView tv_discount = (TextView) view.findViewById(R.id.tv_discout);
        LinearLayout ll_addDrug = (LinearLayout) view.findViewById(R.id.ll_addDrug);
        final Button btn_close = (Button) view.findViewById(R.id.btn_close);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        for (int i = 0; i < data.getDrug_detail().size(); i++) {
            View views = LayoutInflater.from(context).inflate(R.layout.item_drug_spec2, null);
            TextView tv_drugName = (TextView) views.findViewById(R.id.tv_drug);
            TextView tv_drugPrice = (TextView) views.findViewById(R.id.tv_drugPrice);
            TextView tv_drugNum = (TextView) views.findViewById(R.id.tv_drugNum);
            tv_drugName.setText((i + 1) + "." + data.getDrug_detail().get(i).drug + "" + data.getDrug_detail().get(i).specification);
            tv_drugPrice.setText(data.getDrug_detail().get(i).price + "*" + data.getDrug_detail().get(i).drug_num);
            tv_drugNum.setText("合计：" + data.getDrug_detail().get(i).money + "元");
            ll_addDrug.addView(views);
        }
        DrugExtraFee extraFee = data.getCharge();
        if (!extraFee.commission.isEmpty()) {
            tv_extraFee.setVisibility(View.VISIBLE);
            for (int i = 0; i < extraFee.commission.size(); i++) {
                View linearLayout = LayoutInflater.from(context).inflate(R.layout.item_r_grey_text, null);
                TextView tv_commission = (TextView) linearLayout.findViewById(R.id.tv_detail);
                tv_commission.setText(extraFee.commission.get(i));
                ll_add.addView(linearLayout);
            }
        }
        if (!extraFee.extraFee.isEmpty()) {
            tv_extraFee.setVisibility(View.VISIBLE);
            tv_discount.setVisibility(View.VISIBLE);
            for (int i = 0; i < extraFee.extraFee.size(); i++) {
                View linearLayout = LayoutInflater.from(context).inflate(R.layout.item_r_grey_text, null);
                TextView tv_commission = (TextView) linearLayout.findViewById(R.id.tv_detail);
                tv_commission.setText(extraFee.extraFee.get(i));
                ll_add.addView(linearLayout);
            }
        }
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    public void showPayDialog() {
        DialogUtils.showPayDialog(PayPrescriptionActivity.this, binding.tvPay, new DialogUtils.OnPopItemClickListener() {
            @Override
            public void onItemClickListener(int which) {
                String coupon;
                if (getSelectedCoupon() != -1) {
                    coupon = getCouponId();
                } else {
                    coupon = "";
                }
                double money = Double.valueOf(binding.tvNeedPay.getText().toString());

                switch (which) {
                    case 1:
                        id = 1;
                        break;
                    case 2:
                        id = 2;
                        break;
                    case 3:
                        LoadingHelper.showMaterLoading(PayPrescriptionActivity.this, "正在加载...");
                        if (id == 2) {
                            detail.confirmPay(PayPrescriptionActivity.this, PayMethod.WECHAT, coupon, money, DrugListFragment.getDrugExtraField());
                        } else {
                            detail.confirmPay(PayPrescriptionActivity.this, PayMethod.ALIPAY, coupon, money, DrugListFragment.getDrugExtraField());
                        }
                        id = 0;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void selectCoupon() {
        if (coupons == null || coupons.isEmpty()) {
            Toast.makeText(PayPrescriptionActivity.this, "暂时没有可以使用的优惠券", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialDialog.Builder(PayPrescriptionActivity.this)
                .title("选择优惠券(单选)")
                .negativeText("不使用优惠券")
                .items(coupons)
                .itemsCallbackSingleChoice(selectedCoupon, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectedCoupon = which;
                        binding.tvSelectCoupon.setText(getText(detail));
                        double shouldPay = detail.getNeed_pay() - getDiscountMoney();
                        if (shouldPay < 0D) {
                            shouldPay = 0D;
                        }
                        BigDecimal bigDecimal = new BigDecimal(shouldPay).setScale(2, BigDecimal.ROUND_UP);
                        double money = bigDecimal.doubleValue();
                        binding.tvNeedPay.setText(money + "");
                        return true;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectedCoupon = -1;
                    }
                })
                .build().show();
    }

    public String getDrugId() {
        return getIntent().getStringExtra(Constants.DATA);
    }

    public int getSelectedCoupon() {
        return selectedCoupon;
    }

    public void setSelectedCoupon(int selectedCoupon) {
        this.selectedCoupon = selectedCoupon;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public boolean canUseCoupon(DrugOrderDetail data) {
        return data.getUser_coupon_id().equals("0");
    }

    public String getCouponId() {
        if (selectedCoupon == -1) {
            return String.valueOf(-1);
        }
        return coupons.get(selectedCoupon).id;
    }

    String getText(DrugOrderDetail data) {
        if (!canUseCoupon(data)) {
            if (notPay(data)) {
                return "已选取" + data.getCoupon_info().couponMoney + "元优惠券";
            } else {
                return "已使用" + data.getCoupon_info().couponMoney + "元优惠券";
            }
        }
        if (!notPay(data)) {
            return "未使用优惠券";
        }
        if (coupons != null && coupons.size() > 0) {
            if (selectedCoupon != -1) {
                return "已选择" + coupons.get(selectedCoupon).detail();
            } else {
                return "您有" + coupons.size() + "张优惠券可用";
            }
        }
        return "暂时没有可以选择的优惠券";
    }

    public boolean notPay(DrugOrderDetail data) {
        return data.getHas_pay() == IntBoolean.FALSE;
    }

    public double getDiscountMoney() {
        if (canUseCoupon(detail)) {
            if (selectedCoupon == -1) {
                return 0D;
            }
            double discount = Double.parseDouble(coupons.get(selectedCoupon).couponMoney);
            return Math.max(0D, discount);
        } else {
            return 0D;
        }
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction().equals("updateSuccess")) {
                initData(1);
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
