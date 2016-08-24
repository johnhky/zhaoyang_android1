package com.doctor.sun.model;

import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.ChangeMyPhoneNumFragment;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.ItemAddTag;
import com.doctor.sun.vo.ItemPickImage;
import com.doctor.sun.vo.ItemRadioDialog;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput2;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rick on 18/8/2016.
 */

public class EditDoctorInfoModel {
    public static final String TAG = EditDoctorInfoModel.class.getSimpleName();


    public List<SortedItem> parseData(Doctor data) {
        List<SortedItem> result = new ArrayList<>();

        ItemPickImage avatar = new ItemPickImage(R.layout.item_pick_avatar, data.getAvatar());
        avatar.setItemId("avatar");
        avatar.setPosition(result.size());
        result.add(avatar);

        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input2, "姓名", "");
        name.setSubTitle("(必填)");
        name.setResultNotEmpty();
        name.setItemId("name");
        name.setPosition(result.size());
        name.setResult(data.getName());
        result.add(name);

        insertDivider(result);

        if (data.getPhone() == null || data.getPhone().equals("")) {
            final ItemTextInput2 personalPhone = ItemTextInput2.phoneInput("手机号码", "请输入11位手机号码");
            personalPhone.setResultNotEmpty();
            personalPhone.setItemLayoutId(R.layout.item_text_input2);
            personalPhone.setItemId("phone");
            personalPhone.setPosition(result.size());
            personalPhone.setResult(data.getPhone());
            result.add(personalPhone);
        } else {
            ClickMenu changePersonalPhone = new ClickMenu(R.layout.item_change_phone_num, 0, "手机号码", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangeMyPhoneNumFragment.startFrom(view.getContext());
                }
            });
            changePersonalPhone.setSubTitle("更换手机号码");
            changePersonalPhone.setDetail(data.getPhone());
            changePersonalPhone.setPosition(result.size());
            result.add(changePersonalPhone);
        }

        insertDivider(result);

        ItemRadioGroup radioGroup = new ItemRadioGroup(R.layout.item_pick_gender);
        radioGroup.setTitle("性别");
        radioGroup.setResultNotEmpty();
        radioGroup.setItemId("gender");
        radioGroup.setPosition(result.size());
        radioGroup.setSelectedItem(data.getGender());
        result.add(radioGroup);

        insertDivider(result);

        ItemTextInput2 hospital = new ItemTextInput2(R.layout.item_text_input2, "所属医院", "");
        hospital.setResultNotEmpty();
        hospital.setItemId("hospital");
        hospital.setPosition(result.size());
        hospital.setResult(data.getHospitalName());
        result.add(hospital);

        insertDivider(result);

        ItemTextInput2 specialist = new ItemTextInput2(R.layout.item_text_input2, "专科", "");
        specialist.setResultNotEmpty();
        specialist.setItemId("specialist");
        specialist.setPosition(result.size());
        specialist.setResult(data.getSpecialist());
        result.add(specialist);

        insertDivider(result);

        ItemTextInput2 hospitalPhone = ItemTextInput2.phoneInput("医院/科室电话", "请输入11位手机号码");
        hospitalPhone.setCanResultEmpty();
        hospitalPhone.setItemLayoutId(R.layout.item_text_input2);
        hospitalPhone.setItemId("hospitalPhone");
        hospitalPhone.setPosition(result.size());
        hospitalPhone.setResult(data.getHospitalPhone());
        result.add(hospitalPhone);

        insertDivider(result);

        ItemRadioDialog title = new ItemRadioDialog(R.layout.item_pick_title);
        title.setResultNotEmpty();
        title.setTitle("职称");
        title.setItemId("title");
        title.setPosition(result.size());
        String[] stringArray = AppContext.me().getResources().getStringArray(R.array.title_array);
        if (data.getTitle() != null) {
            for (int i = 0; i < stringArray.length; i++) {
                if (data.getTitle().equals(stringArray[i])) {
                    title.setSelectedItem(i + 1);
                    break;
                }
            }
        }
        title.addOptions(stringArray);
        result.add(title);

        insertDivider(result);

        BaseItem tagHeader = new BaseItem(R.layout.item_tag_list);
        tagHeader.setTitle("专长标签");
        tagHeader.setItemId(ItemAddTag.TAGS_START);
        tagHeader.setPosition(result.size());
        result.add(tagHeader);

        for (int i = 0; i < 5; i++) {
            ItemTextInput2 tag = new ItemTextInput2(R.layout.item_edit_tag, "专长标签", "");
            tag.setHint("点击编辑您的个人标签");
            tag.resultCanEmpty();
            tag.setItemId(UUID.randomUUID().toString());
            tag.setPosition(result.size());

            if (i < data.tags.size()) {
                tag.setItemId(data.tags.get(i).tagId);
                String tagName = data.tags.get(i).tagName;
                tag.setResult(tagName);
                if (!Strings.isNullOrEmpty(tagName)) {
                    tag.lockResult();
                }
            }
            result.add(tag);
        }

        ItemAddTag e = new ItemAddTag();
        e.setPosition(result.size());
        tagHeader.setItemId(ItemAddTag.TAGS_START);
        result.add(e);

        insertDivider(result);

        ItemTextInput2 detail = new ItemTextInput2(R.layout.item_text_input4, "个人简介/医治专长", "");
        detail.setItemId("detail");
        detail.setImeOptions(EditorInfo.IME_ACTION_NONE);
        detail.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        detail.setPosition(result.size());
        detail.setResult(data.getDetail());
        detail.setMaxLength(200);
        result.add(detail);

        insertDivider(result);

        insertSpace(result);

        ItemPickImage certifiedImg = new ItemPickImage(R.layout.item_pick_certificate_img, data.getCertifiedImg());
        certifiedImg.setTitle("上传\n执业资格");
        certifiedImg.setItemId("certifiedImg");
        certifiedImg.setPosition(result.size());
        certifiedImg.setSpan(4);
        result.add(certifiedImg);

        ItemPickImage practitionerImg = new ItemPickImage(R.layout.item_pick_certificate_img, data.getPractitionerImg());
        practitionerImg.setTitle("上传\n注册证书");
        practitionerImg.setItemId("practitionerImg");
        practitionerImg.setPosition(result.size());
        practitionerImg.setSpan(4);
        result.add(practitionerImg);

        ItemPickImage titleImg = new ItemPickImage(R.layout.item_pick_certificate_img, data.getTitleImg());
        titleImg.setTitle("上传\n职称证书");
        titleImg.setItemId("titleImg");
        titleImg.setPosition(result.size());
        titleImg.setSpan(4);
        result.add(titleImg);


        ItemTextInput2 imgPs = new ItemTextInput2(R.layout.item_r_text_input, "*上传的相关照片内容需清晰明确", "");
        imgPs.setItemId(UUID.randomUUID().toString());
        imgPs.setPosition(result.size());
        imgPs.setMaxLength(200);
        result.add(imgPs);

        insertDivider(result);

        insertSpace(result);

        return result;
    }


    private void insertDivider(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_marginlr_13dp);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }

    private void insertSpace(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.space_30dp);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }

    public void saveDoctorInfo(SortedListAdapter adapter, Callback<ApiDTO<String>> callback) {
        HashMap<String, String> result = toHashMap(adapter, callback);
        if (result != null) {
            ProfileModule api = Api.of(ProfileModule.class);
            api.editDoctorProfile(result).enqueue(callback);
        }
    }

    private HashMap<String, String> toHashMap(SortedListAdapter adapter, Callback<ApiDTO<String>> callback) {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                ApiDTO<String> body = new ApiDTO<>();
                body.setStatus("500");
                body.setMessage(item.errorMsg());
                callback.onResponse(null, Response.success(body));
                return null;
            }

            String value = item.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }
}
