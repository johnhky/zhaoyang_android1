package com.doctor.sun.ui.widget.vpbs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;

public class ViewPagerBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new ViewPagerBottomSheetDialog(getContext(), getTheme());
    }

}
