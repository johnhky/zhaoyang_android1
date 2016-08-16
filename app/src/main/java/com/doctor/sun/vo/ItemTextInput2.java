package com.doctor.sun.vo;

import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;

import com.doctor.sun.R;


/**
 * Created by rick on 18/3/2016.
 */
public class ItemTextInput2 extends BaseItem {
    private int itemLayoutId;
    private int inputType = InputType.TYPE_CLASS_TEXT;
    private int imeOptions = EditorInfo.IME_ACTION_NEXT;
    private int maxLength = 16;
    private int titleGravity = Gravity.END;
    private String title;
    private String subTitle;
    private String hint;
    private String result;


    public ItemTextInput2(int itemLayoutId, String title, String hint) {
        this.itemLayoutId = itemLayoutId;
        this.title = title;
        this.hint = hint;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getHint() {
        return hint;
    }

    public int getInputType() {
        return inputType;
    }

    public int getImeOptions() {
        return imeOptions;
    }

    public String getTitle() {
        return title;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public void setImeOptions(int imeOptions) {
        this.imeOptions = imeOptions;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getTitleGravity() {
        return titleGravity;
    }

    public void setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
    }


    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public static ItemTextInput2 phoneInput(String title, String hint) {
        ItemTextInput2 viewModel = new ItemTextInput2(R.layout.item_answer_input, title, hint);
        viewModel.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        viewModel.setMaxLength(11);
        viewModel.setInputType(InputType.TYPE_CLASS_PHONE);

        return viewModel;
    }
}
