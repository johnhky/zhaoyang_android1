package com.doctor.sun.vo;

import android.databinding.Bindable;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.vo.validator.RegexValidator;
import com.doctor.sun.vo.validator.Validator;

import java.util.LinkedList;
import java.util.regex.Pattern;

import io.ganguo.library.util.Strings;


/**
 * Created by rick on 18/3/2016.
 */
public class ItemTextInput2 extends BaseItem {
    private int inputType = InputType.TYPE_CLASS_TEXT;
    private int imeOptions = EditorInfo.IME_ACTION_NEXT;
    private int maxLength = 16;
    private int titleGravity = Gravity.END;
    private String title;
    private String subTitle;
    private String hint;
    private String result = "";
    private String error;

    private LinkedList<Validator> validators;


    public ItemTextInput2(int itemLayoutId, String title, String hint) {
        super(itemLayoutId);
        this.title = title;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
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
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        notifyPropertyChanged(BR.error);
    }

    public boolean isResultValid(String result) {
        if (validators == null || validators.isEmpty()) {
            return true;
        }
        if (result == null || result.equals("")) {
            setError("");
            return false;
        }
        for (Validator validator : validators) {
            if (!validator.isValid(result)) {
                setError(validator.errorMsg());
                return false;
            }
        }
        setError("");
        return true;
    }

    public void add(Validator element) {
        if (element == null) {
            return;
        }
        if (validators == null) {
            validators = new LinkedList<>();
        }
        validators.add(element);
    }

    @Bindable
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        notifyPropertyChanged(BR.result);
    }

    public static ItemTextInput2 phoneInput(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_answer_input, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(11);
        viewModel.setInputType(InputType.TYPE_CLASS_PHONE);
        viewModel.add(new RegexValidator(Pattern.compile("^\\d{11}$"), "请输入11位手机号码"));
        viewModel.add(new RegexValidator(Strings.MOBILE_PATTERN, "手机号码格式错误"));

        return viewModel;
    }

    @Override
    public String getValue() {
        return result;
    }
}
