package com.doctor.sun.vm;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;

/**
 * Created by rick on 10/1/2017.
 */
public class ItemDrugDetailBtn extends ItemTextInput2 {
    private BaseListAdapter<SortedItem, ViewDataBinding> adapter;

    public ItemDrugDetailBtn(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
    }

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
}
