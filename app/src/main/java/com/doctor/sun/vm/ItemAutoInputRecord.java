package com.doctor.sun.vm;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.DrugDetail;
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
    public PopupWindow popupWindow;
    public boolean dismissByUser = false;
    List<String> list = new ArrayList<>();
    public MyAdapter mAdapter;
    ListView mListView;

    public ItemAutoInputRecord(int layoutId, String title, String hint) {
        super(layoutId, title, hint);
    }

    public void removeThis(SimpleAdapter simpleAdapter) {
        if (!isEnabled()) {
            return;
        }
        notifyPropertyChanged(BR.removed);
        simpleAdapter.removeItem(this);
        simpleAdapter.notifyDataSetChanged();
    }

    public void showDropDown(final View view) {
        if (dismissByUser) {
            dismissByUser = false;
            return;
        }
        initPopupWindow(view);
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow.showAsDropDown(view);
    }

    private void initPopupWindow(View view) {
        if (popupWindow == null) {
            View views = LayoutInflater.from(view.getContext()).inflate(R.layout.item_listview, null);
            mListView = (ListView) views.findViewById(R.id.list_record);
            mAdapter = new MyAdapter(view.getContext(), getRecordList(getResult()));
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = windowManager.getDefaultDisplay().getWidth() - 200;
            popupWindow = new PopupWindow(views, width, 450);
            popupWindow.setOutsideTouchable(true);
            mListView.setOnItemClickListener(listener);
            popupWindow.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        }else{
            mAdapter = new MyAdapter(view.getContext(), getRecordList(getResult()));
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<String> getRecordList(String text) {
        DrugModule api = Api.of(DrugModule.class);
        api.getRecordList(text).enqueue(new SimpleCallback<List<String>>() {
            @Override
            protected void handleResponse(List<String> response) {
                list.clear();
                if (response.size()> 0) {
                    list.addAll(response);
                }
                notifyPropertyChanged(BR.result);
                if (mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        return list;
    }

    public AdapterView.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
        if (popupWindow != null) {
            popupWindow.dismiss();
            mListView.setOnItemClickListener(this.listener);
        }
    }


    public void dismissDialog() {
        popupWindow.dismiss();
        dismissByUser = true;
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        List<String> list;

        public MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
                holder.tv_name = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(list.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
        }
    }
}
