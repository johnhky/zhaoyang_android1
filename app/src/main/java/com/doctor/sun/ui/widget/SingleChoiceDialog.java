package com.doctor.sun.ui.widget;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.databinding.ItemSingleChoiceBinding;

/**
 * Created by rick on 14/2/2016.
 */
public class SingleChoiceDialog {
    public static void show(Context context, final ItemSingleChoiceBinding binding) {
        new MaterialDialog.Builder(context)
                .title(binding.getTitle())
                .items(binding.getValues().toArray(new String[binding.getValues().size()]))
                .itemsCallbackSingleChoice(binding.getSelectedItem(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View item, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        binding.setSelectedItem(which);
                        binding.etInput.setText(text);
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }
}
