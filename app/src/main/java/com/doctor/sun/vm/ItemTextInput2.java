package com.doctor.sun.vm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.immutables.DrugOrderDetail;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.ui.widget.PickDateDialog;
import com.doctor.sun.util.AddressTask;
import com.doctor.sun.util.DialogUtils;
import com.doctor.sun.vm.validator.RegexValidator;
import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import io.ganguo.library.util.StringsUtils;


/**
 * Created by rick on 18/3/2016.
 */
public class ItemTextInput2 extends BaseItem {
    private int inputType = InputType.TYPE_CLASS_TEXT;
    private int imeOptions = EditorInfo.IME_ACTION_NEXT;
    private int maxLength = 16;
    private int titleGravity = Gravity.END;
    private String subTitle;
    private String hint;
    private String result = "";
    @ColorRes
    private int titleColor = R.color.text_color_black;
    @DimenRes
    private int textSize = R.dimen.font_15;
    private View.OnClickListener listener;
    private boolean clickable;
    private String province, phone, receiver, remark;
    private int background;
    private int selectedItem = -1;
    private ArrayList<String> options = new ArrayList<>();

    public ItemTextInput2(int itemLayoutId, String title, String hint) {
        super(itemLayoutId);
        setTitle(title);
        this.hint = hint;
    }

    @Bindable
    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        notifyPropertyChanged(BR.inputType);
    }

    @Bindable
    public int getImeOptions() {
        return imeOptions;
    }

    public void setImeOptions(int imeOptions) {
        this.imeOptions = imeOptions;
        notifyPropertyChanged(BR.imeOptions);
    }

    @Bindable
    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        notifyPropertyChanged(BR.maxLength);
    }

    @Bindable
    public int getTitleGravity() {
        return titleGravity;
    }

    public void setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        notifyPropertyChanged(BR.titleGravity);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
        notifyChange();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
        notifyChange();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
        notifyChange();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyChange();
    }

    @Bindable
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR.subTitle);
    }

    @Bindable
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        notifyPropertyChanged(BR.hint);
    }

    public void showAddressSelector(final Context context) {
        AddressTask task = new AddressTask((Activity) context);
        task.setHideCounty(false);
        task.setCallback(new AddressTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                Toast.makeText(context, "初始化数据失败!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (province.getAreaName().equals("其他") && city.getAreaName().equals("其他")) {
                    setResult(county.getAreaName());
                    setProvince(county.getAreaName());
                } else {
                    setProvince(province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName());
                    setResult(province.getAreaName());
                    setCity(city.getAreaName());
                    setArea(county.getAreaName());
                }

            }
        });
        task.execute();
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    @Bindable
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        notifyPropertyChanged(BR.result);
    }

    @Override
    public String getValue() {
        if (Strings.isNullOrEmpty(result)) {
            return "";
        }
        if (selectedItem != -1) {
            result = options.get(selectedItem);
        }
        return result;
    }

    public void setTitleColor(@ColorRes int titleColor) {
        this.titleColor = titleColor;
    }

    public int getTitleColor(Context context) {
        return context.getResources().getColor(titleColor);
    }

    public void setTextSize(@DimenRes int textSize) {
        this.textSize = textSize;
    }

    public float getTextSize(Context context) {
        return context.getResources().getDimension(textSize);
    }

    public void toggleEditable() {
        if (isEnabled()) {
            if (!Strings.isNullOrEmpty(getResult())) {
                lockResult();
            }
        } else {
            clearAnswer();
        }
    }

    public void lockResult() {
        setEnabled(false);
    }

    public void clearAnswer() {
        setEnabled(true);
        setResult("");
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (!isEnabled()) {
            return null;
        }
        if (!Strings.isNullOrEmpty(result)) {
            HashMap<String, Object> map = new HashMap<>();
            String key = getKey().replace(QuestionType.fill, "");
            Questions2 questions2 = (Questions2) adapter.get(key);
            map.put("question_id", questions2.answerId);
            map.put("fill_content", result);
            return map;
        } else {
            return null;
        }
    }

    public static ItemTextInput2 mobilePhoneInput(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_answer_input, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(11);
        viewModel.setInputType(InputType.TYPE_CLASS_PHONE);
        viewModel.add(new RegexValidator(Pattern.compile("^\\d{11}$"), "请输入11位手机号码"));
        viewModel.add(new RegexValidator(StringsUtils.MOBILE_PATTERN, "手机号码格式错误"));

        return viewModel;
    }

    @NonNull
    public static ItemTextInput2 newDialogTitle(String title) {
        ItemTextInput2 item = new ItemTextInput2(R.layout.item_dialog_title, title, "");
        item.setItemId("TITLE");
        item.setPosition(0);
        return item;
    }

    public static ItemTextInput2 phoneInput(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_answer_input, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(14);
        viewModel.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
//        viewModel.add(new RegexValidator(StringsUtils.MOBILE_OR_PHONE_PATTERN, "电话号码格式错误"));

        return viewModel;
    }

    public static ItemTextInput2 password(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_text_input5, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(24);
        viewModel.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        return viewModel;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public View.OnClickListener getOnClickListener() {
        return listener;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean getClickable() {
        return clickable;
    }


    private BaseListAdapter<SortedItem, ViewDataBinding> adapter;

    public void showDialog(Context context) {
        if (adapter == null) return;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);

        builder.stackingBehavior(StackingBehavior.ALWAYS)
                .btnStackedGravity(GravityEnum.CENTER)
                .titleGravity(GravityEnum.CENTER)
                .title("收费明细")
                .neutralText("关闭")
                .adapter(adapter, new LinearLayoutManager(context))
                .show();

    }

    public void setAdapter(BaseListAdapter<SortedItem, ViewDataBinding> adapter) {
        this.adapter = adapter;
    }

    public BaseListAdapter<SortedItem, ViewDataBinding> getAdapter() {
        return adapter;
    }

    public void removeOption(int option) {
        options.remove(option);
    }

    public void addOptions(String[] stringArray) {
        Collections.addAll(options, stringArray);
    }


    public void showPopupWindow(View view, Context context) {
        ArrayList<String> items = options;
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items));
        listPopupWindow.setWidth(view.getWidth());
        listPopupWindow.setHeight(450);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                listPopupWindow.dismiss();
                notifyChange();
            }
        });
        listPopupWindow.setAnchorView(view);
        listPopupWindow.show();
    }

    public void pickBirthDay(Context context) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setResult(year + "-" + month + 1);
            }
        }, 2017, 8, 9);
        datePickerDialog.show();
    }

    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
}
