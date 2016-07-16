package com.doctor.sun.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentTextViewBinding;

/**
 * Created by rick on 16/7/2016.
 */

public class TextViewFragment extends BaseFragment {
    public static TextViewFragment newInstance(String text) {

        Bundle args = new Bundle();
        args.putString(Constants.DATA, text);

        TextViewFragment fragment = new TextViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String string = getArguments().getString(Constants.DATA);
        FragmentTextViewBinding inflate = DataBindingUtil.inflate(inflater, R.layout.fragment_text_view, container, true);
        inflate.setData(string);
        return inflate.getRoot();
    }
}
