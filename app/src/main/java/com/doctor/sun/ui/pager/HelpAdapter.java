//package com.doctor.sun.ui.pager;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//import com.doctor.sun.ui.fragment.HelpFragment;
//
//import java.util.List;
//
///**
// * Created by lucas on 2/2/16.
// */
//public class HelpAdapter extends FragmentPagerAdapter {
//    private List<String> data;
//
//    public HelpAdapter(FragmentManager fm, List<String> data) {
//        super(fm);
//        this.data = data;
//    }
//
//    @Override
//    public int getCount() {
//        if (data == null) {
//            return 0;
//        }
//        return data.size() + 1;
//    }
//
//
//    @Override
//    public Fragment getItem(int position) {
//        if (position < data.size()) {
//            return HelpFragment.newInstance(data.get(position));
//        } else {
//            return HelpFragment.newInstance("");
//        }
//    }
//}
