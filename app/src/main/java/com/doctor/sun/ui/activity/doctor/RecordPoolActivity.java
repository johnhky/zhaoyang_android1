package com.doctor.sun.ui.activity.doctor;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 1/6/2016.
 */
public class RecordPoolActivity extends PageActivity2 {
    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    private String keyword = "";

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, RecordPoolActivity.class);
        return intent;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.item_record_pool);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.recordPool(getCallback().getPage(), keyword).enqueue(getCallback());
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何记录";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                getCallback().resetPage();
                loadMore();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }
    @Override
    public int getMidTitle() {
        return R.string.title_record_pool;
    }
}
