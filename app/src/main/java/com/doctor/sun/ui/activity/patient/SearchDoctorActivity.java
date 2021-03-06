package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.City;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.bean.Province;
import com.doctor.sun.databinding.ActivitySearchDoctorBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.NewDoctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.widget.CityPickerDialog;
import com.doctor.sun.util.AnimationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Response;

import static com.doctor.sun.util.AnimationUtils.hideView;

/**
 * Created by rick on 20/1/2016.
 */
public class SearchDoctorActivity extends BaseFragmentActivity2 implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int INTERVAL = 1000;

    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String PROVINCE = "province";
    public static final String LNG = "lng";
    public static final String LAT = "lat";
    public static final String GENDER = "gender";
    public static final String SEARCH = "search";
    public static final String SORT = "sort";
    public static final String SORT_BY = "sortBy";

    private AppointmentModule api = Api.of(AppointmentModule.class);

    private ActivitySearchDoctorBinding binding;
    private SimpleAdapter adapter;
    private PageCallback<Doctor> callback;

    private boolean sortByPoint = false;

    private CityPickerDialog cityPickerDialog;
    private Location location;
    private List<Doctor> favoriteDoctorList;

    private boolean isSearching = false;

    public static Intent makeIntent(Context context, @AppointmentType int type) {
        Intent i = new Intent(context, SearchDoctorActivity.class);
        i.putExtra(Constants.DATA, type);
        return i;
    }

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, SearchDoctorActivity.class);
        return i;
    }


    @AppointmentType
    public int getType() {
        return getIntent().getIntExtra(Constants.DATA, AppointmentType.PREMIUM);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_doctor);
        initHeader();
        initAdapter();
        initRecyclerView();
        initRefreshLayout();
        initFilter();
//        ShowCaseUtil.showCase2(binding.flSearch,"点击搜索您需要预约的医生",-1,2,-1,true);
    }

    private void initRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(this);
    }

    private void initHeader() {
        binding.filter.setOnClickListener(this);
        binding.points.setOnClickListener(this);
        binding.distance.setOnClickListener(this);
        binding.flSearch.setOnClickListener(this);
        binding.search.addTextChangedListener(new TextWatcher() {
            private long lastSearchTime = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                long now = System.currentTimeMillis();
                if (now - lastSearchTime > INTERVAL) {
                    refreshData(sortByPoint);
                    lastSearchTime = now;
                    boolean lastStatus = isSearching;
                    isSearching = !s.toString().isEmpty();
                    if (lastStatus != isSearching) {
                        callback.resetPage();
                    }
                }
            }
        });
    }


    private void initFilter() {
        binding.clearGender.setSelected(true);
        binding.clearGender.setOnClickListener(this);

        binding.male.setOnClickListener(this);
        binding.female.setOnClickListener(this);

        binding.clearTitle.setSelected(true);
        binding.clearTitle.setOnClickListener(this);

        binding.titleOne.setOnClickListener(this);
        binding.titleTwo.setOnClickListener(this);
        binding.titleThree.setOnClickListener(this);
        binding.titleFour.setOnClickListener(this);
        binding.titleFive.setOnClickListener(this);

        binding.tvProvince.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);

        binding.confirm.setOnClickListener(this);
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initAdapter() {
        adapter = new SimpleAdapter();
        adapter.putInt(AdapterConfigKey.APPOINTMENT_TYPE, getType());
        callback = new PageCallback<Doctor>(adapter) {

            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.refreshLayout.setRefreshing(false);
                        if (adapter != null && adapter.isEmpty()) {
                            binding.emptyIndicator.setText("找不到任何医生,请更换搜索条件");
                            binding.emptyIndicator.setVisibility(View.VISIBLE);
                        } else {
                            binding.emptyIndicator.setVisibility(View.GONE);
                        }
                    }
                }, 1000);
            }
        };
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_search_doctor);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                binding.refreshLayout.setRefreshing(true);
                loadMore();
            }
        });

    }

    private void loadMore() {
        api.allDoctors(callback.getPage(), "1",getQueryParam(), getTitleParam()).enqueue(callback);
        /*api.newAllDoctors(callback.getPage(),getQueryParam(),getTitleParam()).equals(callback);*/
    }

/*
    @Deprecated
    private void loadKnowDoctor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    binding.refreshLayout.setRefreshing(true);
                    final Response<ApiDTO<List<Doctor>>> recentDoctors = api.recentDoctors(callback.getPage(), getQueryParam(), getTitleParam()).execute();
                    final Response<ApiDTO<PageDTO<Doctor>>> favoriteDoctors;
                    if (callback.getPage().equals("1")) {
                        favoriteDoctors = api.favoriteDoctors().execute();
                    } else {
                        favoriteDoctors = null;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (recentDoctors.isSuccessful()) {
                                List<Doctor> data = recentDoctors.body().getData();
                                if (data != null && !data.isEmpty()) {
                                    adapter.add(new Description(R.layout.item_description, "最近预约"));
                                    adapter.addAll(data);
                                }
                            }
                            if (favoriteDoctors != null && favoriteDoctors.isSuccessful()) {
                                PageDTO<NewDoctor> data = favoriteDoctors.body().getData();
                                favoriteDoctorList = data.getData();
                                if (favoriteDoctors != null && !favoriteDoctorList.isEmpty()) {
                                    adapter.add(new Description(R.layout.item_description, "我的收藏"));
                                    adapter.addAll(favoriteDoctorList);
                                }
                            }
                            binding.refreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                            if (adapter != null && adapter.isEmpty()) {
                                binding.emptyIndicator.setText("找不到任何医生,请更换搜索条件");
                                binding.emptyIndicator.setVisibility(View.VISIBLE);
                            } else {
                                binding.emptyIndicator.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
*/

    private void refreshData(boolean sortByPoint) {
        this.sortByPoint = sortByPoint;
        callback.resetPage();
        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.onFinishLoadMore(false);
    }

    private HashMap<String, String> getQueryParam() {
        HashMap<String, String> hashMap = new HashMap<>();

        if (sortByPoint) {
            hashMap.put(SORT_BY, "point");
            if (binding.points.isSelected()) {
                hashMap.put(SORT, ASC);
            } else {
                hashMap.put(SORT, DESC);
            }
        } else {
//            hashMap.put(SORT_BY, "inBetweenItemCount");
//            if (binding.inBetweenItemCount.isAnswered()) {
//                hashMap.put(SORT, ASC);
//            } else {
//                hashMap.put(SORT, DESC);
//            }
        }

        hashMap.put(SEARCH, binding.search.getText().toString());

        hashMap.put(PROVINCE, binding.tvProvince.getText().toString());

        if (location != null) {
            hashMap.put(LNG, String.valueOf(location.getLongitude()));
            hashMap.put(LAT, String.valueOf(location.getLatitude()));
        }

        if (binding.male.isSelected() && !binding.female.isSelected()) {
            hashMap.put(GENDER, "1");
        } else if (!binding.male.isSelected() && binding.female.isSelected()) {
            hashMap.put(GENDER, "2");
        } else {
            hashMap.put(GENDER, "");
        }
        return hashMap;
    }

    private ArrayList<Integer> getTitleParam() {
        ArrayList<Integer> result = new ArrayList<>();
        if (binding.titleOne.isSelected()) {
            result.add(1);
            result.add(2);
        }
        if (binding.titleTwo.isSelected()) {
            result.add(3);
        }
        if (binding.titleThree.isSelected()) {
            result.add(4);
            result.add(5);
        }
        if (binding.titleFour.isSelected()) {
            result.add(6);
        }
        if (binding.titleFive.isSelected()) {
            result.add(7);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.points: {
                binding.points.setSelected(!binding.points.isSelected());
                refreshData(true);
                break;
            }
            case R.id.distance: {
                binding.distance.setSelected(!binding.distance.isSelected());
                refreshData(false);
                break;
            }
            case R.id.filter: {
                if (binding.svFilter.getVisibility() == View.GONE) {
                    AnimationUtils.revealView(binding.svFilter);
                } else {
                    hideView(binding.svFilter);
                }
                break;
            }
            case R.id.confirm: {
                if (binding.svFilter.getVisibility() == View.GONE) {
                } else {
                    hideView(binding.svFilter);
                    refreshData(sortByPoint);
                }
                break;
            }
            case R.id.fl_search: {
                if (binding.search.getVisibility() == View.GONE) {
                    AnimationUtils.revealView(binding.search);
                    binding.search.requestFocus();
                    Systems.showKeyboard(getWindow(), binding.search);
                }
                break;
            }
            case R.id.clear_gender: {
                binding.gender.dispatchSetSelected(false);
                binding.clearGender.setSelected(true);
                break;
            }
            case R.id.clear_title: {
                binding.title.dispatchSetSelected(false);
                binding.clearTitle.setSelected(true);
                break;
            }
            case R.id.title_one:
            case R.id.title_two:
            case R.id.title_three:
            case R.id.title_four:
            case R.id.title_five: {
                v.setSelected(!v.isSelected());
                binding.clearTitle.setSelected(false);
                break;
            }
            case R.id.male:
            case R.id.female: {
                v.setSelected(!v.isSelected());
                binding.clearGender.setSelected(false);
                break;
            }
            case R.id.tv_province:
            case R.id.tv_city: {
                if (cityPickerDialog == null) {
                    createCityPicker();
                }
                cityPickerDialog.show();
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (binding.search.getVisibility() == View.VISIBLE) {
            hideView(binding.search);
            binding.search.clearFocus();
            Systems.hideKeyboard(this);
        } else if (binding.svFilter.getVisibility() == View.VISIBLE) {
            hideView(binding.svFilter);
        } else {
            super.onBackPressed();
        }
    }


    private void createCityPicker() {
        RealmResults<Province> provinces = getRealm().where(Province.class).findAll();
        String state = binding.tvProvince.getText().toString();
        String city = binding.tvCity.getText().toString();
        int provinceId = 0;
        int cityId = 0;
        for (int i = 0; i < provinces.size(); i++) {
            if (provinces.get(i).getState().equals(state)) {
                provinceId = i;
            }
        }
        RealmList<City> cities = provinces.get(provinceId).getCities();
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getCity().equals(city)) {
                cityId = i;
            }
        }

        cityPickerDialog = new CityPickerDialog(this, provinces, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.tvCity.setText(cityPickerDialog.getCity());
                binding.tvProvince.setText(cityPickerDialog.getProvince());
                cityPickerDialog.dismiss();
            }
        });

        cityPickerDialog.setProvinceId(provinceId);
        cityPickerDialog.setCityId(cityId);
    }

    protected void updateLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onRefresh() {
        callback.setPage(1);
        adapter.clear();
        adapter.notifyDataSetChanged();
        loadMore();
    }
}
