package com.doctor.sun.model;

import android.content.Intent;
import android.databinding.Observable;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;
import com.doctor.sun.vm.ItemPickDate;
import com.doctor.sun.vm.ItemRadioDialog;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;
import com.doctor.sun.vm.ItemTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by kb on 16-9-18.
 */

public class NewMedicalRecordModel {
    private String[] relativeList = AppContext.me().getResources().getStringArray(R.array.relative_array);
    private List<String>items = new ArrayList<>();
    public NewMedicalRecordModel() {

    }

    public List<SortedItem> parseItem(final SortedListAdapter adapter, int type) {
        final List<SortedItem> result = new ArrayList<>();
        final ItemRadioDialog.TextEvaluator evaluator = new ItemRadioDialog.TextEvaluator();
            /*ItemTextInput2 relation = new ItemTextInput2(R.layout.item_text_input2, "您与患者的关系", "");*/
        final ItemRadioDialog relation = new ItemRadioDialog(R.layout.item_record_text);
        relation.setResultNotEmpty();
        relation.setTitle("您是患者的");
        relation.setItemId("relation");
        relation.setEvaluator(evaluator);
        relation.setSelectedItem(-1);
        items = Arrays.asList(relativeList);
        relation.addOptions(items);
        relation.setPosition(result.size());
        result.add(relation);
        boolean isSelt = relation.getSelectedItemText().equals("本人");
        ModelUtils.insertDividerMarginLR(result);
        final ItemTextInput2 selfName = new ItemTextInput2(R.layout.item_text_input5, "您的姓名", "必填");
        selfName.setPosition(result.size());
        selfName.setItemId("selfName");
        if (isSelt){
            selfName.setResultNotEmpty();
        }else{
            selfName.setCanResultEmpty();
        }
        result.add(selfName);
        ModelUtils.insertDividerMarginLR(result);

        selfName.setVisible(!isSelt);
        final ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input5, "患者姓名", "必填");
        name.setVisible(!isSelt);
        name.setItemId("name");
        name.setPosition(result.size());
        result.add(name);
        ModelUtils.insertDividerMarginLR(result);

        ItemPickDate date = new ItemPickDate(R.layout.item_pick_birthmonth, "出生年月");
        date.setItemId("birthday");
        date.setPosition(result.size());
        result.add(date);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioGroup genderGroup = new ItemRadioGroup(R.layout.item_pick_gender);
        genderGroup.setTitle("性别");
        genderGroup.setResultNotEmpty();
        genderGroup.setItemId("gender");
        genderGroup.setPosition(result.size());
        result.add(genderGroup);

        ModelUtils.insertDividerMarginLR(result);
        ModelUtils.insertSpace(result, R.layout.divider_30dp);

        ItemTextView textView = new ItemTextView(R.layout.item_button_truth, "");
        textView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> medicalRecord = ModelUtils.toHashMap(adapter, new SimpleCallback() {
                    @Override
                    protected void handleResponse(Object response) {

                    }
                });
                ProfileModule api = Api.of(ProfileModule.class);
                if (medicalRecord != null) {
                    if (medicalRecord.get("relation").equals("本人")) {
                        api.setSelfMedicalRecord(medicalRecord).enqueue(new SimpleCallback<MedicalRecord>() {
                            @Override
                            protected void handleResponse(MedicalRecord response) {
                                Toast.makeText(AppContext.me(), "病历创建成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setAction("CREATE_REGISTER_SUCCESS");
                                AppContext.me().sendBroadcast(intent);
                            }
                        });
                    } else {
                        api.setRelativeMedicalRecord(medicalRecord).enqueue(new SimpleCallback<MedicalRecord>() {
                            @Override
                            protected void handleResponse(MedicalRecord response) {
                                Toast.makeText(AppContext.me(), "病历创建成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setAction("CREATE_REGISTER_SUCCESS");
                                AppContext.me().sendBroadcast(intent);
                            }
                        });
                    }
                }

            }
        });
        if (type == 1) {
            textView.setPosition(result.size());
            result.add(textView);
        }
        return result;
    }


    public void saveMedicalRecord(SortedListAdapter adapter, int recordType, Callback<ApiDTO<MedicalRecord>> callback) {
        HashMap<String, String> medicalRecord = ModelUtils.toHashMap(adapter, callback);
        if (medicalRecord != null) {
            ProfileModule api = Api.of(ProfileModule.class);
            if (recordType == NewMedicalRecordFragment.TYPE_SELF) {
                api.setSelfMedicalRecord(medicalRecord).enqueue(callback);
            } else {
                api.setRelativeMedicalRecord(medicalRecord).enqueue(callback);
            }

        }
    }

}