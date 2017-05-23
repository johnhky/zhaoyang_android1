package com.doctor.sun.ui.activity.patient;

import android.content.Intent;
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
import com.doctor.sun.entity.Address;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.UploadAddressModel;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.BaseFragment;
import com.doctor.sun.ui.fragment.SortedListFragment;
import com.doctor.sun.vm.BaseItem;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;

/**
 * Created by heky on 17/4/26.
 */
@Factory(type = BaseFragment.class, id = "AddressAddFragment")
public class AddressAddFragment extends SortedListFragment {
    public static final String TAG = AddressAddFragment.class.getSimpleName();
    private ProfileModule api = Api.of(ProfileModule.class);
    private UploadAddressModel model;

    public static String UPLOAD = "1";
    public static String UPDATE ="2";

    Address address = new Address();

    public static Bundle getBundle(String data){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME,TAG);
        bundle.putString(Constants.ADDRESS,data);
        return bundle;
    }
    public static Bundle getUpdateBundle(Address address){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME,TAG);
        bundle.putSerializable(Constants.DATA,address);
        return bundle;
    }

    public static Bundle upload(){
        Bundle bundle = getBundle(UPLOAD);
        return  bundle;
    }
    public static Bundle update(Address address){
        Bundle bundle = getUpdateBundle(address);
        return bundle;
}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new UploadAddressModel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        disableRefresh();

        if (getArguments().getSerializable(Constants.DATA)!=null){
            address = (Address) getArguments().getSerializable(Constants.DATA);
        }
        List<SortedItem>list = model.parseItem(address);
        getAdapter().insertAll(list);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if (address.getId()!=0){
                    updateAddress();
                }else {
                    uploadAddress();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void uploadAddress(){
        api.uploadAddress(toHashMap(getAdapter())).enqueue(new SimpleCallback<Address>() {
            @Override
            protected void handleResponse(Address response) {
                Toast.makeText(getContext(),"添加成功！",Toast.LENGTH_SHORT).show();
                Intent to = new Intent();
                to.setAction("addAddress");
                getActivity().sendBroadcast(to);
                getActivity().finish();
            }
        });
    }
    public void updateAddress(){
    api.updateAddress(toHashMap(getAdapter())).enqueue(new SimpleCallback<Address>() {
        @Override
        protected void handleResponse(Address response) {
            Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
            Intent to = new Intent();
            to.setAction("addAddress");
            getActivity().sendBroadcast(to);
            getActivity().finish();
        }
    });
    }

    private HashMap<String,String> toHashMap(SortedListAdapter adapter){
        HashMap<String ,String> result = new HashMap<>();
        for (int i = 0 ; i <adapter.size() ;i++){
            BaseItem baseItem = (BaseItem) adapter.get(i);
            if (!baseItem.isValid("")){
                if (!baseItem.resultCanEmpty()){
                    baseItem.addNotNullOrEmptyValidator();
                }
                Toast.makeText(getContext(),baseItem.errorMsg(),Toast.LENGTH_SHORT).show();
                return  null;
            }
            String value = baseItem.getValue();
            if (!Strings.isNullOrEmpty(value)){
                String key = baseItem.getKey();
                if (!Strings.isNullOrEmpty(key)){
                    if (key.equals("defaults")){
                        if (baseItem.isEnabled()==true){
                            value = "1";
                        }else{
                            value ="0";
                        }
                    }
                    if (key.equals("province")){
                        if (null!=baseItem.getCity()&&""!=baseItem.getCity()){
                            result.put("city",baseItem.getCity());
                            if(null!=baseItem.getArea()&&""!=baseItem.getArea()) {
                                result.put("area",baseItem.getArea());
                            }
                        }

                    }
                    result.put(key,value);
                }

            }
        }

        return result;
    }

}
