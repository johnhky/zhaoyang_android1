package com.doctor.sun.vm;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vm.validator.RegexValidator;
import com.google.common.base.Strings;

import java.util.HashMap;
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

    @ColorRes
    private int titleColor = R.color.text_color_black;
    @DimenRes
    private int textSize = R.dimen.font_15;
    private View.OnClickListener listener;
    private boolean clickable;
    private boolean haveAlertMaxLength = false;

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
        if (result != null && result.length() >= maxLength && result.length() != 0 && !haveAlertMaxLength) {
            Toast.makeText(AppContext.me(), "字数不能大于" + maxLength + "位", Toast.LENGTH_SHORT).show();
            haveAlertMaxLength = true;
        } else {
            haveAlertMaxLength = false;
        }
        notifyPropertyChanged(BR.result);
    }

    @Override
    public String getValue() {
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
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_text_input2, title, hint);
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
}
