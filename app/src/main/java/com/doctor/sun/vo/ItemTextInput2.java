package com.doctor.sun.vo;

import android.databinding.Bindable;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.vo.validator.RegexValidator;
import com.google.common.base.Strings;

import java.util.regex.Pattern;

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
        return result;
    }

    public void toggleEditable() {
        if (isEnabled()) {
            if (!Strings.isNullOrEmpty(getResult())) {
                lockResult();
            } else {
                return;
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

    public static ItemTextInput2 phoneInput(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_answer_input, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(11);
        viewModel.setInputType(InputType.TYPE_CLASS_PHONE);
        viewModel.add(new RegexValidator(Pattern.compile("^\\d{11}$"), "请输入11位手机号码"));
        viewModel.add(new RegexValidator(StringsUtils.MOBILE_PATTERN, "手机号码格式错误"));

        return viewModel;
    }


    public static ItemTextInput2 password(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_text_input2, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(24);
        viewModel.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        viewModel.add(new RegexValidator(Pattern.compile("^\\s*(?:\\S\\s*){6,}$"), "请输入6位字符串密码"));

        return viewModel;
    }
}
