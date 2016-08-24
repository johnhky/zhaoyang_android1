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
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.PageActivity2;


/**
 * Created by rick on 11/20/15.
 */
public class AppointmentListActivity extends PageActivity2 {
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private String keyword;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AppointmentListActivity.class);
        return i;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.searchAppointment(getCallback().getPage(), keyword, "").enqueue(getCallback());
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

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有预约任何患者";
    }

    @Override
    public int getMidTitle() {
        return R.string.title_appointment_list;
    }

}
