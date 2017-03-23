package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.auto.Factory;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.bean.MobEventId;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Token;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.WebBrowserActivity;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.CountDownUtil;
import com.doctor.sun.util.MD5;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ClickMenu;
import com.doctor.sun.vm.ItemCaptchaInput;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;
import com.doctor.sun.vm.validator.RegexValidator;
import com.doctor.sun.vm.validator.Validator;
import com.google.common.base.Strings;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import retrofit2.Call;

/**
 * Created by rick on 19/8/2016.
 */

@Factory(type = BaseFragment.class, id = "RegisterFragment")
public class RegisterFragment extends SortedListFragment {
    public static final String TAG = RegisterFragment.class.getSimpleName();

    private static final String TYPE_PATIENT = "1";
    private static final String TYPE_DOCTOR = "2";

    public static void startFrom(Context context) {
        context.startActivity(intentFor(context));
    }

    public static Intent intentFor(Context context) {
        String title;
    /*    if (BuildConfig.IS_DOCTOR == IntBoolean.NOT_GIVEN) {
            title = "注册";
        } else*/
        if (BuildConfig.IS_DOCTOR == IntBoolean.TRUE) {
            title = "医生注册";
        } else {
            title = "患者注册";
        }
        return SingleFragmentActivity.intentFor(context, title, getArgs());
    }

    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        disableRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<BaseItem> sortedItems = new ArrayList<>();
        // 放在这里是为了监听registerType切换注册身份时，取消选中CheckBox
        final ClickMenu registerPolicy = new ClickMenu(R.layout.item_register_policy, 0, "我已阅读【注册须知】", null);
 /*       if (BuildConfig.IS_DOCTOR == IntBoolean.NOT_GIVEN) {
            insertSpace(sortedItems);
           /* final ItemRadioGroup registerType = new ItemRadioGroup(R.layout.item_pick_register_type);
            registerType.setSelectedItem(AuthModule.DOCTOR_TYPE);
            registerType.setResultNotEmpty();
            registerType.setTitle("注册类型");
            registerType.setResultNotEmpty();
            registerType.setItemId("type");
            registerType.setPosition(sortedItems.size());
            sortedItems.add(registerType);

            final String doctorRemarks = "*注册为医生，可以通过昭阳医生服务更多的患者";
            final String patientRemarks = "*注册为患者，可以通过昭阳医生找到更多的名医";
            final ItemTextInput2 imgPs = new ItemTextInput2(R.layout.item_r_orange_text, doctorRemarks, "");
            imgPs.setTitleGravity(Gravity.START);
            imgPs.setItemId(UUID.randomUUID().toString());
            imgPs.setPosition(sortedItems.size());
            imgPs.setMaxLength(200);
            registerType.setListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (registerType.getSelectedItem() == AuthModule.DOCTOR_TYPE) {
                        imgPs.setTitle(doctorRemarks);
                    } else if (registerType.getSelectedItem() == AuthModule.PATIENT_TYPE) {
                        imgPs.setTitle(patientRemarks);
                    }
                    registerPolicy.setEnabled(false);
                }
            });
            sortedItems.add(imgPs);

            BaseItem baseItem = new BaseItem();
            baseItem.setItemLayoutId(R.layout.divider_8dp_gray);
            sortedItems.add(baseItem);
        }*/
        final ItemTextInput2 newPhoneNum = ItemTextInput2.mobilePhoneInput("手机号码", "请输入11位手机号码");
        newPhoneNum.setResultNotEmpty();
        newPhoneNum.setItemLayoutId(R.layout.item_text_input2);
        newPhoneNum.setItemId("phone");
        sortedItems.add(newPhoneNum);

        insertDivider(sortedItems);

        final ItemCaptchaInput captcha = new ItemCaptchaInput(R.layout.item_input_captcha, "验证码", "请输入验证码");
        captcha.setResultNotEmpty();
        captcha.setListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(final View v) {
                if (!newPhoneNum.isValid("")) {
                    Toast.makeText(v.getContext(), newPhoneNum.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthModule profileModule = Api.of(AuthModule.class);

                profileModule.sendCaptcha(newPhoneNum.getResult()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        CountDownUtil.countDown((TextView) v, "重新获取(%d)", "获取验证码", 60);
                    }
                });
            }
        });
        captcha.setSubTitle("获取验证码");
        captcha.add(new RegexValidator(Pattern.compile("^\\s*(?:\\S\\s*){6,}$"), "请输入6位字符串验证码"));
        captcha.setMaxLength(6);
        captcha.setItemId("captcha");
        sortedItems.add(captcha);

        insertDivider(sortedItems);

        final ItemTextInput2 passwordOne = ItemTextInput2.password("设置密码", "请输入6~10位数字和字母组合");
        passwordOne.setResultNotEmpty();
        passwordOne.add(new RegexValidator(Pattern.compile("(?=\\S+$).{6,10}$"), "请输入6~10位数字和字母组合"));
        passwordOne.add(new RegexValidator(Pattern.compile("(.)*(\\d)(.)*"), "密码里面应该最少包含一个数字"));
        passwordOne.add(new RegexValidator(Pattern.compile("(.)*[a-zA-Z](.)*"), "密码里面应该最少包含一个字母"));
        passwordOne.setItemLayoutId(R.layout.item_text_input2);
        passwordOne.setItemId("password");
        sortedItems.add(passwordOne);

        insertDivider(sortedItems);

        ItemTextInput2 passwordTwo = ItemTextInput2.password("请再次输入密码", "请输入6~10位数字和字母组合");
        passwordTwo.setResultNotEmpty();
        passwordTwo.setItemLayoutId(R.layout.item_text_input2);
        passwordTwo.setItemId(UUID.randomUUID().toString());
        passwordTwo.add(new Validator() {
            @Override
            public boolean isValid(String input) {
                return passwordOne.getValue().equals(input);
            }

            @Override
            public String errorMsg() {
                return "密码跟第一个密码不相同";
            }
        });
        sortedItems.add(passwordTwo);

        insertDivider(sortedItems);

        registerPolicy.setEnabled(false);
        registerPolicy.setItemId(UUID.randomUUID().toString());
        registerPolicy.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRegistrationPolicy(view.getContext());
            }
        });
        registerPolicy.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setHasOptionsMenu(registerPolicy.isEnabled());
            }
        });
        sortedItems.add(registerPolicy);

        for (int i = 0; i < sortedItems.size(); i++) {
            sortedItems.get(i).setPosition(i);
        }

        getAdapter().insertAll(sortedItems);
        binding.swipeRefresh.setRefreshing(false);
    }

    private void insertDivider(List<BaseItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_start13_end13);
        item.setItemId(UUID.randomUUID().toString());
        list.add(item);
    }

    private void insertSpace(List<BaseItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_30dp);
        item.setItemId(UUID.randomUUID().toString());
        list.add(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_next, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next: {
                registerDone();
            }
        }
        return super.onOptionsItemSelected(item);
    }

/*
    private void done() {
        if (BuildConfig.IS_DOCTOR == IntBoolean.NOT_GIVEN) {
            final String registerType;
            if (isDoctor()) {
                registerType = "医生";
            } else {
                registerType = "患者";
            }
            new MaterialDialog.Builder(getContext())
                    .title("您注册的身份为" + registerType)
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Log.e(TAG, "onClick: 确定注册身份" + registerType);
                            registerDone();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .build().show();
        } else {
            registerDone();
        }
    }
*/

    private void registerDone() {
        AuthModule api = Api.of(AuthModule.class);
        api.register(toHashMap(getAdapter())).enqueue(new SimpleCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                if (isDoctor()) {
                    registerDoctorSuccess(getContext(), response);
                    MobclickAgent.onEvent(getContext(), MobEventId.DOCTOR_REGISTRATION);
                } else if (isPatient()) {
                    registerPatientSuccess(getContext(), response);
                    MobclickAgent.onEvent(getContext(), MobEventId.PATIENT_REGISTRATION);
                }
            }

            @Override
            public void onFailure(Call<ApiDTO<Token>> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(getContext(),"请求失败，请先检查您的网络!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isDoctor() {
        if (BuildConfig.IS_DOCTOR == IntBoolean.TRUE) {
            return true;
        } else if (BuildConfig.IS_DOCTOR == IntBoolean.NOT_GIVEN) {
            String typeString = getAdapter().get("type").getValue();
            if (Strings.isNullOrEmpty(typeString)) {
                return true;
            }
            int type = Integer.parseInt(typeString);
            return type == AuthModule.DOCTOR_TYPE;
        } else {
            return false;
        }
    }

    private boolean isPatient() {
        if (BuildConfig.IS_DOCTOR == IntBoolean.FALSE) {
            return true;
        } else if (BuildConfig.IS_DOCTOR == IntBoolean.NOT_GIVEN) {
            String typeString = getAdapter().get("type").getValue();
            if (Strings.isNullOrEmpty(typeString)) {
                return true;
            }
            int type = Integer.parseInt(typeString);
            return type == AuthModule.PATIENT_TYPE;
        } else {
            return false;
        }

    }


    private HashMap<String, String> toHashMap(SortedListAdapter adapter) {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                Toast.makeText(getContext(), item.errorMsg(), Toast.LENGTH_SHORT).show();
                return null;
            }

            String value = item.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    if (key.equals("password")) {
                        result.put(key, MD5.getMessageDigest(value.getBytes()));
                    } else {
                        result.put(key, value);
                    }
                }
            }
        }

        if (BuildConfig.IS_DOCTOR == IntBoolean.TRUE) {
            result.put("type", TYPE_DOCTOR);
        } else if (BuildConfig.IS_DOCTOR == IntBoolean.FALSE) {
            result.put("type", TYPE_PATIENT);
        }

        return result;
    }


    private void registerPatientSuccess(Context context, Token response) {
        if (response != null) {
            TokenCallback.handleToken(response);
            Intent i = PMainActivity2.makeIntent(context);
            context.startActivity(i);
        }
    }

    private void registerDoctorSuccess(Context context, Token response) {
        if (response == null) {
            //TODO
            editDoctorInfo(context);
        } else {
            TokenCallback.handleToken(response);
            editDoctorInfo(context);
        }
    }

    public void editDoctorInfo(Context context) {
        Toast.makeText(context, "注册成功,请完善您的个人信息!", Toast.LENGTH_SHORT).show();
        Doctor data = new Doctor();
        data.setPhone(getAdapter().get("phone").getValue());
        Intent intent = EditDoctorInfoFragment.intentFor(context, data);
        context.startActivity(intent);
    }

    public void viewRegistrationPolicy(Context context) {
        String url = BuildConfig.BASE_URL + "readme/registration-policy";
        if (isDoctor()) {
            url += "?client=doctor";
        }
        Intent i = WebBrowserActivity.intentFor(context, url, "注册须知");
        context.startActivity(i);
    }
}
