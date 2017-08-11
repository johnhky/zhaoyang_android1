package com.doctor.sun.vm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.DrugInfo;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AutoComplete;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 28/10/2016.
 */

public class ItemAutoCompleteTextInput<T> extends ItemTextInput2 {
    public static final String TAG = ItemAutoCompleteTextInput.class.getSimpleName();
    private AdapterView.OnItemClickListener listener;
    private PopupWindow popupWindow;
    public boolean dismissByUser = false;
    ListView mListView;
    public MyAdapter arrayAdapter;
    public List<DrugInfo> list = new ArrayList<>();

    public ItemAutoCompleteTextInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
    }

    public void showDropDown(View view) {
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
            View views = LayoutInflater.from(AppContext.me()).inflate(R.layout.item_listview, null);
            mListView = (ListView) views.findViewById(R.id.list_record);
            arrayAdapter = new MyAdapter(view.getContext(), getEntries(getResult()));
            mListView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = windowManager.getDefaultDisplay().getWidth() - 200;
            popupWindow = new PopupWindow(views, width, 500);
            popupWindow.setOutsideTouchable(true);
            mListView.setOnItemClickListener(listener);
            popupWindow.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        } else {
            arrayAdapter = new MyAdapter(AppContext.me(), getEntries(getResult()));
            mListView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public List<DrugInfo> getEntries(String text) {
        AutoComplete complete = Api.of(AutoComplete.class);
        complete.drugInfo(text).enqueue(new SimpleCallback<List<DrugInfo>>() {
            @Override
            protected void handleResponse(List<DrugInfo> response) {
                list.clear();
                if (response.size() > 0) {
                    list.addAll(response);
                }
                notifyPropertyChanged(BR.result);
                if (arrayAdapter != null) {
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        return list;
    }

    @NonNull
    public List<DrugInfo> getFilteredEntries() {
        return getEntries("");
    }

    public AdapterView.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
        if (popupWindow != null) {
            mListView.setOnItemClickListener(this.listener);
        }
    }


    public void dismissDialog() {
        popupWindow.dismiss();
        dismissByUser = true;
    }

    public class MyAdapter extends BaseAdapter {
        Context context;
        List<DrugInfo> list;

        public MyAdapter(Context context, List<DrugInfo> list) {
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
            holder.tv_name.setText(list.get(position).getDrug_name());
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
        }
    }
}
