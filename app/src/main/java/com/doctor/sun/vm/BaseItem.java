package com.doctor.sun.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vm.validator.NotNullOrEmptyValidator;
import com.doctor.sun.vm.validator.Validator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by rick on 24/12/2015.
 */
public class BaseItem extends BaseObservable implements LayoutId, SortedItem, Validator {

    private static final boolean ALWAYS_VISIBLE = true;
    private static final boolean WHEN_RESULT_IS_EMPTY = false;

    public static final int NOT__EMPTY_OR_NULL = 0;
    public static final int UNSPECIFIED = 1;
    public static final int CAN__EMPTY_OR_NULL = 2;

    private String title;
    private boolean errorVisibleMode = WHEN_RESULT_IS_EMPTY;
    private int resultVerifyMode = CAN__EMPTY_OR_NULL;
    private LinkedList<Validator> validators;
    private String error;

    private String itemId;

    private boolean userSelected = false;
    protected boolean enabled = true;
    private boolean visible = true;
    private int itemLayoutId = -1;
    private long position;
    private int span = 12;
    private String action = "填写";

    public BaseItem() {
    }

    public BaseItem(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public void removeThis(SortedListAdapter adapter) {
        notifyPropertyChanged(BR.removed);
        adapter.removeItem(this);
    }

    @Override
    public int getItemLayoutId() {
        if (isVisible()) {
            return itemLayoutId;
        } else {
            return R.layout.item_empty;
        }
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyChange();
    }

    @JsonIgnore
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyChange();
    }

    public void setEnabledDontNotify(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    @Bindable
    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @JsonIgnore
    public String getItemId() {
        return itemId;
    }

    @JsonIgnore
    @Override
    public int getLayoutId() {
        return getItemLayoutId();
    }

    @JsonIgnore
    @Override
    public long getCreated() {
        return -position;
    }

    @JsonIgnore
    @Override
    public String getKey() {
        return itemId;
    }

    @JsonIgnore
    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @JsonIgnore
    @Override
    public String getValue() {
        return "";
    }

    @JsonIgnore
    private int getResultVerifyMode() {
        return resultVerifyMode;
    }

    private void setResultVerifyMode(int resultVerifyMode) {
        this.resultVerifyMode = resultVerifyMode;
    }

    @JsonIgnore
    public boolean resultCanEmpty() {
        return getResultVerifyMode() != NOT__EMPTY_OR_NULL;
    }

    public void setResultNotEmpty() {
        setResultVerifyMode(NOT__EMPTY_OR_NULL);
    }

    public void setCanResultEmpty() {
        setResultVerifyMode(CAN__EMPTY_OR_NULL);
    }

    public void addNotNullOrEmptyValidator() {
        resultVerifyMode = UNSPECIFIED;
        errorVisibleMode = ALWAYS_VISIBLE;
        add(new NotNullOrEmptyValidator("请填写必填项目" + getTitle()));
    }

    public void setError(String error) {
        this.error = error;
        notifyPropertyChanged(BR.error);
    }

    @JsonIgnore
    public boolean isValid(String ignoredInput) {
        String result = getValue();
        boolean isResultEmpty = Strings.isNullOrEmpty(result);
        if (isResultEmpty) {
            if (resultVerifyMode == NOT__EMPTY_OR_NULL) {
                setError("请" + getAction() + "必填项目" + getTitle());
                return false;
            } else if (resultVerifyMode == CAN__EMPTY_OR_NULL) {
                setError("");
                return true;
            }
        }

        if (validators == null || validators.isEmpty()) {
            return true;
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

    @JsonIgnore
    @Override
    public String errorMsg() {
        return error;
    }

    @JsonIgnore
    @Bindable
    public String getError() {
        return error;
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

    /**
     * @param regiteredField 在xml里面给一个Bindable的值来通知error改变状态
     * @return
     */
    public boolean errorVisible(String regiteredField) {
        if (errorVisibleMode != ALWAYS_VISIBLE) {
            return !Strings.isNullOrEmpty(getValue()) && !isValid("");
        } else {
            return !isValid("");
        }
    }

    @JsonIgnore
    @Override
    public int getSpan() {
        return span;
    }

    @JsonIgnore
    @Override
    @Bindable
    public boolean isUserSelected() {
        return userSelected;
    }

    public void setUserSelected(boolean userSelected) {
        this.userSelected = userSelected;
        notifyPropertyChanged(BR.userSelected);
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }

    @JsonIgnore
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
