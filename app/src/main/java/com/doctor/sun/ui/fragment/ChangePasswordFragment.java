package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.ChangePasswordModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.List;

/**
 * Created by kb on 16-12-8.
 */
@Factory(type = BaseFragment.class, id = "ChangePasswordFragment")
public class ChangePasswordFragment extends SortedListNoRefreshFragment {
    public static final String TAG = ChangePasswordFragment.class.getSimpleName();

    private ChangePasswordModel model;

    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ChangePasswordModel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();

        List<SortedItem> items = model.parseData();
        getAdapter().insertAll(items);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirm, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                save();
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        model.save(getAdapter(), new SimpleCallback() {
            @Override
            protected void handleResponse(Object response) {
                Toast.makeText(getActivity(), "成功修改密码", Toast.LENGTH_SHORT).show();

                getActivity().finish();
            }
        });
    }
}
