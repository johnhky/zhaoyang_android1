package com.doctor.sun.vm;

import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.doctor.sun.BR;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 17/5/5.
 */

public class ItemAutoInputRecord extends ItemTextInput2 {

    private AdapterView.OnItemClickListener listener;
    private ListPopupWindow popupWindow;
    private boolean dismissByUser = false;
    List<String> list = new ArrayList<>();
    public ArrayAdapter<String> mAdapter;
    public ItemAutoInputRecord(int layoutId, String title, String hint) {
        super(layoutId, title, hint);
    }

    public void removeThis(SimpleAdapter simpleAdapter){
        if (!isEnabled()){
            return;
        }
        notifyPropertyChanged(BR.removed);
        simpleAdapter.removeItem(this);
        simpleAdapter.notifyDataSetChanged();
    }

    public void showDropDown(View view) {
        if (dismissByUser) {
            dismissByUser = false;
            return;
        }
        initPopupWindow(view);
        mAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,getRecordList());
        mAdapter.notifyDataSetChanged();
        popupWindow.setAdapter(mAdapter);
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow.show();
    }

    private void initPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new ListPopupWindow(view.getContext());
            popupWindow.setAnchorView(view);
            popupWindow.setOnItemClickListener(listener);
            popupWindow.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        }
    }

    public List<String> getRecordList() {
        DrugModule api = Api.of(DrugModule.class);
        api.getRecordList(getResult()).enqueue(new SimpleCallback<List<String>>() {
            @Override
            protected void handleResponse(List<String> response) {
                if (response.size() > 0) {
                    if (mAdapter!=null){
                        list.clear();
                        list.addAll(response);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        list.addAll(response);
                    }
                }
            }
        });
        return list;
    }

    public List<String>getRecord(){
        return list;
    }

    public AdapterView.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
        if (popupWindow != null) {
            popupWindow.setOnItemClickListener(this.listener);
        }
    }


    public void dismissDialog() {
        popupWindow.dismiss();
        dismissByUser = true;
    }

}
