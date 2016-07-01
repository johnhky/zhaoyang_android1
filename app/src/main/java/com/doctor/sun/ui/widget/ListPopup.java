//package com.doctor.sun.ui.widget;
//
//import android.content.Context;
//import android.support.v7.widget.LinearLayoutManager;
//import android.view.LayoutInflater;
//import android.widget.PopupWindow;
//
//import com.doctor.sun.R;
//import com.doctor.sun.databinding.DialogListBinding;
//import com.doctor.sun.http.callback.PageCallback;
//import com.doctor.sun.ui.adapter.SimpleAdapter;
//import com.doctor.sun.ui.adapter.core.LoadMoreListener;
//
///**
// * Created by rick on 6/6/2016.
// */
//public class ListPopup extends PopupWindow {
//    private Context context;
//    private DialogListBinding binding;
//    private SimpleAdapter adapter;
//    private PageCallback<Object> callback;
//
//    public ListPopup(Context context) {
//        super(context,null, R.style.dialogNoTitle);
//        this.context = context;
//        binding = DialogListBinding.inflate(LayoutInflater.from(context));
//        setContentView(binding.getRoot());
//        initAdapter();
//        initRecyclerView();
//    }
//
//
//    protected void initRecyclerView() {
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerView.setAdapter(adapter);
//    }
//
//    protected void initAdapter() {
//        adapter = new SimpleAdapter(getContext());
//        adapter.onFinishLoadMore(true);
//        callback = new PageCallback<>(adapter);
//        adapter.setLoadMoreListener(new LoadMoreListener() {
//            @Override
//            protected void onLoadMore() {
//                loadMore();
//            }
//        });
//
//    }
//
//    protected void loadMore() {
//    }
//
//    protected void refresh() {
//        callback.resetPage();
//        adapter.clear();
//        adapter.notifyDataSetChanged();
//        adapter.onFinishLoadMore(false);
//        loadMore();
//    }
//
//    public DialogListBinding getBinding() {
//        return binding;
//    }
//
//    public SimpleAdapter getAdapter() {
//        return adapter;
//    }
//
//    public void setAdapter(SimpleAdapter adapter) {
//        this.adapter = adapter;
//    }
//
//    public PageCallback getCallback() {
//        return callback;
//    }
//
//    public void setCallback(PageCallback callback) {
//        this.callback = callback;
//    }
//
//    public Context getContext() {
//        return context;
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }
//}
