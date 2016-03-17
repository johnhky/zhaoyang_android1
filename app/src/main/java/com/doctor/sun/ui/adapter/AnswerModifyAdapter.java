package com.doctor.sun.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemAnswerBinding;
import com.doctor.sun.databinding.ItemEditBinding;
import com.doctor.sun.databinding.ItemImageBinding;
import com.doctor.sun.databinding.ItemPrescriptionBinding;
import com.doctor.sun.databinding.ItemRadioBinding;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.widget.FlowLayout;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 填写问卷 编辑答案 adapter
 * Created by Lynn on 1/19/16.
 */
public class AnswerModifyAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Logger logger = LoggerFactory.getLogger(AnswerModifyAdapter.class);
    //区分填写数字的部分量词表, 用于显示hint
    private static final HashMap<Character, Boolean> PARAM_CLASSIFIER = new HashMap<>();
    private Context mActivity;
    //记录当前需要添加药品或者上传图片的position, 方便回调
    private int needPillsOrImages = -1;

    public AnswerModifyAdapter(Context context) {
        super(context);
        mActivity = context;
        setUpMapKey();
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, int position) {
        vh.getBinding().setVariable(BR.position, String.valueOf(position));
        if (vh.getItemViewType() == R.layout.item_answer) {
            Answer answer = (Answer) get(position);
            final ItemAnswerBinding binding = (ItemAnswerBinding) vh.getBinding();
            binding.flReset.setVisibility(View.GONE);
            binding.tvAddPills.setVisibility(View.GONE);

            setLocalComponent(binding, answer, position);

            if (answer.getNeedRefill() == 1) {
                binding.ivPosition.setImageResource(R.drawable.bg_msg_count);
            } else if (answer.getIsFill() == 1) {
                binding.ivPosition.setImageResource(R.drawable.shape_position);
            } else {
                binding.ivPosition.setImageResource(R.drawable.bg_position);
            }
        }

        super.onBindViewBinding(vh, position);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder<ViewDataBinding> holder) {
        if (holder.getLayoutPosition() == getItemCount() - 1) {
            //最后一项设置底部的padding
            holder.getBinding().getRoot().setPadding(0, 0, 0,
                    (int) getContext().getResources().getDimension(R.dimen.dp_130));
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder<ViewDataBinding> holder) {
        //恢复padding
        if (holder.getLayoutPosition() == getItemCount() - 1) {
            holder.getBinding().getRoot().setPadding(0, 0, 0, 0);
        }
        super.onViewDetachedFromWindow(holder);
    }

    private void setLocalComponent(ItemAnswerBinding binding, Answer answer, int position) {
        binding.flAnswer.removeAllViews();

        switch (answer.getQuestion().getQuestionType()) {
            case "fills": {
                setPills(binding, answer, position);
                break;
            }
            case "fill": {
                setFill(binding, answer);
                break;
            }
            case "uploads": {
                uploadImages(binding, answer, position);
                break;
            }
            case "checkbox": {
                boxAnswer(binding, answer);
                break;
            }
            case "radio": {
                radioAnswer(binding, answer);
                break;
            }
            default: {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * 药品类型
     *
     * @param binding
     * @param answer
     * @param position
     */
    private void setPills(final ItemAnswerBinding binding, final Answer answer, final int position) {
        //填写药物
        binding.tvAddPills.setVisibility(View.VISIBLE);

        //恢复历史记录
        if (answer.getAnswerContent() != null && answer.getAnswerContent() instanceof List) {
            List<Object> content = (List<Object>) answer.getAnswerContent();
            for (int i = 0; i < content.size(); i++) {
                Prescription data = null;
                try {
                    data = JacksonUtils.fromMap((LinkedHashMap) content.get(i), Prescription.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!answer.getPrescriptions().contains(data)) {
                    answer.getPrescriptions().add(data);
                }
            }
        }

        if (answer.getPrescriptions().size() > 0) {
            for (int i = 0; i < answer.getPrescriptions().size(); i++) {
                binding.flAnswer.addView(getPrescriptionView(binding, answer, answer.getPrescriptions().get(i)),
                        binding.flAnswer.getChildCount() - 1);
            }
        }

        binding.tvAddPills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditPrescriptionActivity.makeIntent(mActivity, null);
                needPillsOrImages = position;
                ((Activity) mActivity).startActivityForResult(intent, Constants.PATIENT_PRESCRITION_REQUEST_CODE);
            }
        });

    }

    /**
     * 描述性回答
     *
     * @param binding
     * @param answer
     */
    @SuppressWarnings("unchecked")
    private void setFill(final ItemAnswerBinding binding, final Answer answer) {
        //填写文本
        ItemEditBinding editBinding = ItemEditBinding.inflate(getInflater(), binding.flAnswer, true);
        editBinding.etAnswer.setTextColor(Color.parseColor("#363636"));
        editBinding.etAnswer.setBackgroundResource(R.drawable.shape_edit);

        if (answer.getAnswerContent() instanceof List) {
            List<String> content = (List<String>) answer.getAnswerContent();
            editBinding.etAnswer.setText(content.get(0));
        }

        editBinding.etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                List<String> content = new ArrayList<>();
                content.add(s.toString());
                answer.setAnswerContent(content);

                if (!s.toString().equals("")) {
                    setPositionFill(binding, answer);
                } else {
                    clearPositionFill(binding, answer);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    /**
     * 图片类型
     *
     * @param binding
     * @param answer
     * @param position
     */
    private void uploadImages(ItemAnswerBinding binding, final Answer answer, final int position) {
        FlowLayout flowLayout = new FlowLayout(getContext());
        binding.flAnswer.addView(flowLayout, -1);

        //有历史记录
        if (answer.getAnswerContent() instanceof List) {
            try {
                List<String> content = (List<String>) answer.getAnswerContent();
                for (int i = 0; i < content.size(); i++) {
                    if (!answer.getImageUrls().contains(content.get(i))) {
                        logger.d("old image url: " + content.get(i));
                        answer.getImageUrls().add(content.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (answer.getImageUrls().size() > 0) {
            //显示图片
            for (int i = 0; i < answer.getImageUrls().size(); i++) {
                final int index = i;
                final ItemImageBinding imageBinding = ItemImageBinding.inflate(getInflater(), flowLayout, true);
                imageBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //预览图片
                        getContext().startActivity(
                                ImagePreviewActivity.makeIntent(getContext(), answer.getImageUrls().get(index)));
                    }
                });

                imageBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //删除图片
                        needPillsOrImages = position;
                        TwoSelectorDialog.showTwoSelectorDialog(mActivity, "是否删除图片?", "取消", "确定", new TwoSelectorDialog.GetActionButton() {
                            @Override
                            public void onClickPositiveButton(TwoSelectorDialog dialog) {
                                //删除对应图片
                                dialog.dismiss();
                                if (needPillsOrImages != -1 && getItemViewType(needPillsOrImages) == R.layout.item_answer) {
                                    Answer answer = (Answer) get(needPillsOrImages);
                                    if (answer.getImageUrls().contains(answer.getImageUrls().get(index))) {
                                        answer.getImageUrls().remove(answer.getImageUrls().get(index));
                                        answer.setAnswerContent(answer.getImageUrls());
                                        if (answer.getImageUrls().size() == 0) {
                                            answer.setIsFill(1);
                                        }
                                        notifyItemChanged(needPillsOrImages);
                                    }
                                }
                            }

                            @Override
                            public void onClickNegativeButton(TwoSelectorDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        return true;
                    }
                });
                imageBinding.setData(answer.getImageUrls().get(i));
            }
        }
        //上传图片button
        ItemImageBinding uploadBinding = ItemImageBinding.inflate(getInflater(), flowLayout, true);
        uploadBinding.practitionerImg.setBackgroundResource(R.drawable.ic_upload);

        uploadBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needPillsOrImages = position;
                PickImageDialog.chooseImage((Activity) mActivity, Constants.UPLOAD_REQUEST_CODE);
            }
        });
    }

    @SuppressWarnings("unchecked")
    /**
     * 单选类型
     *
     * @param binding
     * @param answer
     */
    private void radioAnswer(final ItemAnswerBinding binding, final Answer answer) {
        //单项选择
        final ItemRadioBinding radioBinding = ItemRadioBinding.inflate(getInflater(), binding.flAnswer, true);

        for (int i = 0; i < answer.getQuestion().getOptions().size(); i++) {
            radioBinding.rgAnswer.addView(getRadio(i));
            setContent(radioBinding, answer, i);
        }

        if (answer.getAnswerContent() instanceof List) {
            List<String> type = (List<String>) answer.getAnswerType();
            List<String> content = (List<String>) answer.getAnswerContent();
            List<Integer> index = new ArrayList<>();
            //显示点击状态
            for (int i = 0; i < answer.getQuestion().getOptions().size(); i++) {
                Options option = answer.getQuestion().getOptions().get(i);
                if (type.size() > 0 && type.get(0).equals(option.getOptionType())) {
                    index.add(i);
                    if (option.getOptionContent().contains("{fill}")) {
                        EditText etContent = (EditText) (radioBinding.getRoot()).findViewWithTag(i);
                        if (etContent != null) {
                            etContent.setText(content.get(index.indexOf(i)));
                        }
                    }
                    ((RadioButton) radioBinding.rgAnswer
                            .getChildAt(i)).setChecked(true);
                    ((RadioButton) radioBinding.rgAnswer
                            .getChildAt(i)).setTextColor(
                            getContext().getResources().getColor(R.color.colorPrimaryDark));
                    break;
                }
            }
            answer.setIndex(index);
        }

        radioBinding.rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //上一次答案记录
                List<String> type = new ArrayList<>();
                List<String> content = new ArrayList<>();
                List<Integer> index = new ArrayList<>();

                for (int i = 0; i < group.getChildCount(); i++) {
                    ((RadioButton) group.getChildAt(i)).setTextColor(i == checkedId ?
                            getContext().getResources().getColor(R.color.colorPrimaryDark)
                            : Color.parseColor("#898989"));

                    if (i == checkedId) {
                        //因为RadioButton，每次有且只有一个可选，会覆盖记录，不需要删除
                        Options option = answer.getQuestion().getOptions().get(i);
                        if (option != null) {
                            type.add(option.getOptionType());
                            index.add(i);
                            //记录当前答案
                            if (!option.getOptionContent().contains("{fill}")) {
                                content.add(option.getOptionContent());
                            } else {
                                EditText etContent = (EditText) (radioBinding.getRoot()).findViewWithTag(i);
                                if (etContent != null) {
                                    content.add(etContent.getText().toString());
                                }
                            }
                            answer.setAnswerMark(option.getOptionMark());
                            answer.setAnswerContent(content);
                            answer.setAnswerType(type);
                            answer.setIndex(index);

                            //有记录
                            setPositionFill(binding, answer);
                        }
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    /**
     * 多项选择类型
     *
     * @param binding
     * @param answer
     */
    private void boxAnswer(final ItemAnswerBinding binding, final Answer answer) {
        final ItemEditBinding editBinding = ItemEditBinding.inflate(getInflater(), binding.flAnswer, true);
        editBinding.etAnswer.setVisibility(View.GONE);
        //装下checkbox列表的容器
        final LinearLayout box = new LinearLayout(getContext());
        box.setOrientation(LinearLayout.VERTICAL);
        ((RelativeLayout) editBinding.getRoot()).addView(box, 0);

        //临时记录填写历史
        final List<String> type = new ArrayList<>();
        final List<String> content = new ArrayList<>();
        final List<Integer> index = new ArrayList<>();

        final int excIndex = boxingAndReturnClearId(editBinding, answer);

        for (int i = 0; i < box.getChildCount(); i++) {
            final int position = i;
            final Options option = answer.getQuestion().getOptions().get(i);

            ((CheckBox) box.getChildAt(i)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean needRefresh = false;
                    buttonView.setTextColor(isChecked ?
                            getContext().getResources().getColor(R.color.colorPrimaryDark)
                            : Color.parseColor("#898989"));

                    if (isChecked) {
                        //如果记录为空 或 没有该记录，则添加记录
                        if (index.size() == 0 || !index.contains(buttonView.getId())) {
                            needRefresh = true;
                            //点击clearOption情况
                            if (excIndex == buttonView.getId()) {
                                //清空其他点击状态
                                clearCheckBoxExcept(box, excIndex);
                            } else if (index.size() > 0 && index.contains(excIndex)) {
                                //excId != -1 && 只有clearOption选中
                                ((CheckBox) box.getChildAt(excIndex)).setChecked(false);
                            }
                            //常规选项
                            if (!option.getOptionContent().contains("{fill}")) {
                                content.add(option.getOptionContent());
                            } else {
                                EditText etContent = (EditText) (editBinding.getRoot()).findViewWithTag(position);
                                if (etContent != null) {
                                    content.add(etContent.getText().toString());
                                }
                            }
                            type.add(option.getOptionType());
                            index.add(buttonView.getId());
                            answer.setAnswerMark(answer.getAnswerMark() + option.getOptionMark());

                            //设置更改状态
                            setPositionFill(binding, answer);
                        }
                    } else {
                        //isChecked == false;
                        if (index.size() > 0 && index.contains(buttonView.getId())) {
                            needRefresh = true;
                            //删除历史记录
                            type.remove(option.getOptionType());
                            //根据相对位置来删除，避免{fill}的情况没有删除
                            content.remove(index.indexOf(buttonView.getId()));
                            //要手动装箱，否则会优先调用 根据index删除对应item的方法
                            index.remove((Integer) buttonView.getId());
                            answer.setAnswerMark(answer.getAnswerMark() - option.getOptionMark());
                        }
                    }

                    //更新记录
                    if (needRefresh) {
                        answer.setAnswerType(type);
                        answer.setAnswerContent(content);
                        answer.setIndex(index);
                    }
                }
            });
        }

        //恢复历史记录
        if (answer.getAnswerContent() instanceof List) {
            content.addAll((List<String>) answer.getAnswerContent());
            type.addAll((List<String>) answer.getAnswerType());
            for (int i = 0; i < answer.getQuestion().getOptions().size(); i++) {
                Options option = answer.getQuestion().getOptions().get(i);
                if (type.contains(option.getOptionType())) {
                    index.add(i);
                    if (option.getOptionContent().contains("{fill}")) {
                        EditText etContent = (EditText) (editBinding.getRoot()).findViewWithTag(i);
                        if (etContent != null) {
                            etContent.setText(content.get(index.indexOf(i)));
                        }
                    }
                    ((CheckBox) box.getChildAt(i)).setChecked(true);
                }
            }
            answer.setIndex(index);
        }
    }

    private View getPrescriptionView(final ItemAnswerBinding binding, final Answer answer, final Prescription data) {
        final ItemPrescriptionBinding prescriptionBinding = ItemPrescriptionBinding.inflate(LayoutInflater.from(getContext()),
                binding.flAnswer, false);
        prescriptionBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.flAnswer.removeView(prescriptionBinding.getRoot());
                if (answer.getPrescriptions().contains(data)) {
                    answer.getPrescriptions().remove(data);
                    //用药信息为空
                    if (answer.getPrescriptions().size() == 0) {
                        clearPositionFill(binding, answer);
                    }
                }
            }
        });
        prescriptionBinding.setData(data);
        return prescriptionBinding.getRoot();
    }

    private RadioButton getRadio(int index) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setGravity(Gravity.CENTER_VERTICAL);
        radioButton.setButtonDrawable(getDrawable(R.drawable.ic_tick, 16, 16));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        radioButton.setPadding(16, 24, 16, 24);

        radioButton.setTextColor(Color.parseColor("#898989"));
        radioButton.setId(index);

        return radioButton;
    }

    private CheckBox getCheckBox(int index) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setPadding(16, 24, 16, 24);
        checkBox.setCompoundDrawablePadding(8);
        checkBox.setButtonDrawable(getDrawable(R.drawable.ic_tick, 16, 16));
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        //index 作为 id
        checkBox.setId(index);
        checkBox.setTextColor(Color.parseColor("#898989"));

        return checkBox;
    }

    private void setContent(final ViewDataBinding binding, Answer answer, int position) {
        // RadioGroup extends LinearLayout
        CompoundButton compoundButton = (CompoundButton) ((LinearLayout)
                ((RelativeLayout) binding.getRoot()).getChildAt(0)).getChildAt(position);
        Options option = answer.getQuestion().getOptions().get(position);

        StringBuilder stringBuilder = new StringBuilder();
        //A. XXX
        stringBuilder.append(option.getOptionType())
                .append(".  ");

        if (option.getOptionContent().contains("{fill}")) {
            //前面有文字的情况
            stringBuilder.append(option.getOptionContent().substring(0, option.getOptionContent().indexOf("{fill}")));
            //添加描述性回答界面
            ((RelativeLayout) binding.getRoot()).addView(getEditTextLayout(compoundButton, answer, position),
                    ((RelativeLayout) binding.getRoot()).getChildCount());
        } else {
            //正常的content
            stringBuilder.append(option.getOptionContent());
        }

        compoundButton.setText(stringBuilder);
    }

    /**
     * 新建对应CheckBox加入容器，返回ClearOption对应Id
     * 如果clearOption 为null, 则返回 －1
     *
     * @return
     */
    private int boxingAndReturnClearId(ViewDataBinding binding, Answer answer) {
        int excId = -1;
        LinearLayout box = (LinearLayout) ((RelativeLayout) binding.getRoot()).getChildAt(0);

        for (int i = 0; i < answer.getQuestion().getOptions().size(); i++) {
            final Options option = answer.getQuestion().getOptions().get(i);
            //加入到容器中
            box.addView(getCheckBox(i));
            setContent(binding, answer, i);

            //确定clearOption
            if (answer.getQuestion().getClearOption() != null
                    && answer.getQuestion().getClearOption().equals(option.getOptionType())) {
                excId = i;
            }
        }

        return excId;
    }

    @SuppressWarnings("unchecked")
    /**
     * 根据需要的view的相对位置来设置描述性回答界面layout
     * position 是 当前compoundButton在容器中的位置, 不是问卷答案的位置
     *
     * @param relativeView
     * @return
     */
    private View getEditTextLayout(final CompoundButton compoundButton, final Answer answer, final int position) {
        final ItemEditBinding editBinding = ItemEditBinding.inflate(LayoutInflater.from(getContext()));
        //根据position标识Tag
        editBinding.etAnswer.setTag(compoundButton.getId());

        if (answer.getIndex() != null) {
            //根据历史记录的index列表找到对应在content中存放的位置
            final int index = answer.getIndex().indexOf(position);
            if (index != -1) {
                editBinding.etAnswer.setText(((List<String>) answer.getAnswerContent()).get(index));
            }
        }

        editBinding.etAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editBinding.etAnswer.setTextColor(Color.parseColor("#363636"));
                    editBinding.etAnswer.setBackgroundResource(R.drawable.shape_edit);

                    compoundButton.setChecked(true);
                } else {
                    editBinding.etAnswer.setTextColor(Color.parseColor("#898989"));
                    editBinding.etAnswer.setBackgroundResource(R.drawable.shape_pre_edit);
                }
            }
        });

        editBinding.etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!compoundButton.isChecked()) {
                    compoundButton.setChecked(true);
                } else {
                    //已经点击, 修改答案内容
                    ((List<String>) answer.getAnswerContent())
                            .set(answer.getIndex().indexOf(compoundButton.getId()), s.toString());
                }
            }
        });

        //截取 "A. " 长度 调整edittext位置
        Options option = answer.getQuestion().getOptions().get(position);
        int endIndex = option.getOptionContent().indexOf("{fill}");
        final int width;
        if (endIndex != -1 && endIndex != 0) {
            //前面有其他文字的情况，需要额外计算这部分的位移
            for (char c : option.getOptionContent().substring(endIndex + 5).toCharArray()) {
                if (PARAM_CLASSIFIER.get(c) != null && PARAM_CLASSIFIER.get(c)) {
                    editBinding.etAnswer.setHint(option.getOptionContent().substring(endIndex).replace("{fill}", "多少"));
                    break;
                }
            }

            width = (int) Layout.getDesiredWidth("A. " + option.getOptionContent().substring(0, endIndex + 1),
                    compoundButton.getPaint());
        } else {
            if (compoundButton instanceof CheckBox) {
                editBinding.etAnswer.setHint("其他");
            }
            width = (int) Layout.getDesiredWidth("A. ", compoundButton.getPaint());
        }

        compoundButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                compoundButton.getViewTreeObserver().removeOnPreDrawListener(this);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) editBinding.getRoot().getLayoutParams();
                lp.setMargins(
                        compoundButton.getLeft() + compoundButton.getPaddingLeft() + compoundButton.getPaddingRight() + width,
                        compoundButton.getTop() - editBinding.getRoot().getPaddingTop(),
                        0,
                        0);
                editBinding.getRoot().setLayoutParams(lp);
                return true;
            }
        });

        return editBinding.getRoot();
    }

    private Drawable getDrawable(int drawId, int width, int height) {
        Drawable drawable = getContext().getResources().getDrawable(drawId);
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
            return drawable;
        }
        return null;
    }

    private void clearCheckBoxExcept(ViewGroup box, int index) {
        for (int i = 0; i < box.getChildCount(); i++) {
            if (i == index) {
                continue;
            }

            ((CheckBox) box.getChildAt(i)).setChecked(false);
        }
    }

    /**
     * 添加药品
     *
     * @param prescription
     */
    public void addPrescription(final Prescription prescription) {
        if (needPillsOrImages != -1 && getItemViewType(needPillsOrImages) == R.layout.item_answer) {
            Answer answer = (Answer) get(needPillsOrImages);
            answer.getPrescriptions().add(prescription);
            answer.setIsFill(0);
            notifyItemChanged(needPillsOrImages);
        }
        needPillsOrImages = -1;
    }

    /**
     * 上传图片
     *
     * @param imageUrl
     */
    public void addImage(String imageUrl) {
        logger.d(needPillsOrImages);
        if (needPillsOrImages != -1 && getItemViewType(needPillsOrImages) == R.layout.item_answer) {
            Answer answer = (Answer) get(needPillsOrImages);
            logger.d("return image url: " + imageUrl);
            answer.getImageUrls().add(imageUrl);
            answer.setIsFill(0);
            notifyItemChanged(needPillsOrImages);
        }
        needPillsOrImages = -1;
    }

    /**
     * 将答案列表转换成Json
     *
     * @return
     */
    public String toJsonAnswer() {
        HashMap<String, Object> answerList = new HashMap<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == R.layout.item_answer) {
                Object addOn = saveAnswer(i);
                if (addOn != null) {
                    answerList.put(((Answer) get(i)).getQuestionId() + "", addOn);
                }
            }
        }
        if (answerList.size() > 0) {
            return JacksonUtils.toJson(answerList);
        } else {
            ToastHelper.showMessage(getContext(), "请填写答案");
            return null;
        }
    }

    /**
     * 保存答案到恰当的结构
     *
     * @param position
     */
    private Object saveAnswer(int position) {
        Answer answer = (Answer) get(position);
        switch (answer.getQuestion().getQuestionType()) {
            case "fills": {
                return savePills(answer);
            }
            case "fill": {
                return saveFill(answer);
            }
            case "uploads": {
                return saveUpload(answer);
            }
            case "checkbox": {
                return saveButton(answer);
            }
            case "radio": {
                return saveButton(answer);
            }
            default: {
                break;
            }
        }
        return null;
    }

    public Object savePills(Answer answer) {
        HashMap<String, Object> prescriptionAnswer = new HashMap<>();
        if (answer.getPrescriptions().size() > 0 || answer.getAnswerContent() instanceof List) {
            prescriptionAnswer.put("content", answer.getPrescriptions());
        } else {
            return null;
        }
        return prescriptionAnswer;
    }

    /**
     * 填写可以为空
     *
     * @param answer
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object saveFill(Answer answer) {
        HashMap<String, List<String>> fillAnswer = new HashMap<>();
        if (answer.getAnswerContent() != null && answer.getAnswerContent() instanceof List) {
            fillAnswer.put("content", (List<String>) answer.getAnswerContent());
        } else {
            return null;
        }
        return fillAnswer;
    }

    public Object saveUpload(Answer answer) {
        HashMap<String, Object> uploadAnswer = new HashMap<>();
        if (answer.getImageUrls().size() > 0 || answer.getAnswerContent() instanceof List) {
            uploadAnswer.put("content", answer.getImageUrls());
        } else {
            return null;
        }
        return uploadAnswer;
    }

    @SuppressWarnings("unchecked")
    public Object saveButton(Answer answer) {
        HashMap<String, Object> boxAnswer = new HashMap<>();
        //content & type
        if (answer.getIndex() != null && answer.getIndex().size() > 0) {
            //按小到大顺序加入答案
            if (answer.getIndex().size() > 1) {
                //选择题目数为1, 或者单选情况不需要排序
                insertionSortWithIndex(answer);
            }

            List<String> contents = (List<String>) answer.getAnswerContent();
            if (contents.contains("")) {
                ToastHelper.showMessage(getContext(), "描述性选项为空");
                return null;
            }

            boxAnswer.put("content", answer.getAnswerContent());
            boxAnswer.put("type", answer.getAnswerType());
            //目前设置全为0
            boxAnswer.put("mark", 0);
        } else {
            return null;
        }
        return boxAnswer;
    }

    @SuppressWarnings("unchecked")
    /**
     * 答案按照index题号从小到大排序, 一般题目数量低于10, 使用插入排序
     *
     * @param answer
     */
    private void insertionSortWithIndex(Answer answer) {
        for (int i = 0; i < answer.getIndex().size(); i++) {
            for (int j = i; j > 0 && answer.getIndex().get(j) < answer.getIndex().get(j - 1); j--) {
                Collections.swap(answer.getIndex(), j, j - 1);
                Collections.swap((List<String>) answer.getAnswerContent(), j, j - 1);
                Collections.swap((List<String>) answer.getAnswerType(), j, j - 1);
            }
        }
    }

    private void setPositionFill(ItemAnswerBinding binding, Answer answer) {
        if (answer.getIsFill() == 1) {
            answer.setIsFill(0);
            binding.ivPosition.setImageResource(R.drawable.bg_position);
        }
    }

    private void clearPositionFill(ItemAnswerBinding binding, Answer answer) {
        answer.setIsFill(1);
        binding.ivPosition.setImageResource(R.drawable.shape_position);
    }

    private void setUpMapKey() {
        //根据部分量词调整hint文本
        final char[] classifier = new char[]{
                '个', '位', '粒', '颗', '袋', '份',
                '片', '次', '遍', '块', '瓶', '种'};

        for (char c : classifier) {
            PARAM_CLASSIFIER.put(c, true);
        }
    }
}
