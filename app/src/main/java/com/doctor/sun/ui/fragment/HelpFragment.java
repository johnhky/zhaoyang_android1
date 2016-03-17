package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.doctor.sun.databinding.FragmentHelpBinding;

/**
 * Created by lucas on 2/3/16.
 */
public class HelpFragment extends android.support.v4.app.Fragment {
    public static final String IMAGE_ID = "IMAGE_ID";
    private FragmentHelpBinding binding;

    public static HelpFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(IMAGE_ID, id);
        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public int getDrawableId() {
        int imageId = getArguments().getInt(IMAGE_ID);
        return imageId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(inflater, container, false);
        Glide.with(this).load(getDrawableId()).into(binding.ivHelp);
        return binding.getRoot();
    }
}