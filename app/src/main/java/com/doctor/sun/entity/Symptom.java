package com.doctor.sun.entity;

import android.databinding.BaseObservable;
import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 12/21/15.
 */
public class Symptom extends BaseObservable implements LayoutId {
    public static final String TAG = Symptom.class.getSimpleName();


    private int itemLayoutId;

    private String title;
    private String others;
    private List<String> values;
    private int selectedItem = 0;
    private SparseBooleanArray states = new SparseBooleanArray();


    public Symptom() {
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyChange();
    }

    public SparseBooleanArray getStates() {
        return states;
    }

    public void setStates(HashMap<String, String> from) {
        for (int i = 0; i < from.size() - 1; i++) {
            String s = from.get(String.valueOf(i));
            if (null != s) {
                states.put(i, s.equals("1"));
            }
        }
        setOthers(from.get("otherContent"));
        notifyChange();
    }

    public void setStates(ArrayList<Integer> from) {

        for (int i = 0; i < from.size(); i++) {
            states.put(i, i == 1);
        }
        notifyChange();
    }

    public void toggle(View view) {
        int id = Integer.parseInt(view.getTag().toString());
        if (id == 0) {
            states.clear();
            states.put(0, true);
        } else {
            states.put(0, false);
            states.put(id, !states.get(id));
        }
        notifyChange();
    }

    public void select(final View view) {
        new MaterialDialog.Builder(view.getContext())
                .title(title)
                .items(values.toArray(new String[values.size()]))
                .itemsCallbackSingleChoice(getSelectedItem(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View item, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        setSelectedItem(which);
                        TextView others = (TextView) view.findViewById(R.id.et_others);
                        others.setText(text);
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }

    @Override
    public String toString() {
        return "Symptom{" +
                "itemLayoutId=" + itemLayoutId +
                ", title='" + title + '\'' +
                ", others='" + others + '\'' +
                ", values=" + values +
                ", selectedItem=" + selectedItem +
                ", states=" + states +
                '}';
    }

    public String toStates() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (null == others) {
            others = " ";
        }
        builder.append("\"");
        builder.append("otherContent");
        builder.append("\":\"");
        builder.append(others);
        if (states.size() != 0) {
            builder.append("\",");
        } else {
            builder.append("\"");
        }
        for (int i = 0; i < states.size(); i++) {
            builder.append("\"");
            builder.append(i);
            builder.append("\":\"");
            int isSelected = states.get(i) ? 0 : 1;
            builder.append(isSelected);
            if (i != states.size() - 1)
                builder.append("\",");
            else {
                builder.append("\"");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public TextViewBindingAdapter.AfterTextChanged getTextChange() {
        return new TextViewBindingAdapter.AfterTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                others = s.toString();
            }
        };
    }
}
