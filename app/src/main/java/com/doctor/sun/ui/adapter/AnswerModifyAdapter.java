package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemAnswerBinding;
import com.doctor.sun.databinding.ItemEditBinding;
import com.doctor.sun.databinding.ItemImageBinding;
import com.doctor.sun.databinding.ItemPrescriptionBinding;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Question;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.activity.ItemSelectHospital;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.widget.FlowLayout;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemPickDate;

import java.util.ArrayList;
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
    private boolean isEditMode = true;

    public AnswerModifyAdapter(Context context, boolean isEditMode) {
        super(context);
        mActivity = context;
//        mapLayout(R.layout.item_answer,R.layout.item_answer2);
        setUpMapKey();
        this.isEditMode = isEditMode;
    }

    public AnswerModifyAdapter(Context context) {
        super(context);
        mActivity = context;
//        mapLayout(R.layout.item_answer,R.layout.item_answer2);
        setUpMapKey();
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, int position) {
        vh.getBinding().setVariable(BR.position, String.valueOf(position));
        if (vh.getItemViewType() == R.layout.item_answer) {
            Answer answer = (Answer) get(position);
            final ItemAnswerBinding binding = (ItemAnswerBinding) vh.getBinding();
            binding.flReset.setVisibility(View.GONE);

            setLocalComponent(binding, answer, position);
        }

        super.onBindViewBinding(vh, position);
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
//                boxAnswer(binding, answer);
                break;
            }
            case "radio": {
//                radioAnswer(binding, answer);
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
//        binding.tvAddPills.setVisibility(View.VISIBLE);

        //恢复历史记录
        if (!answer.isDrugInit) {
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
            answer.isDrugInit = true;
        }

        if (answer.getPrescriptions().size() > 0) {
            binding.flAnswer.removeAllViews();
            for (int i = 0; i < answer.getPrescriptions().size(); i++) {
                View prescriptionView = getPrescriptionView(binding, answer, answer.getPrescriptions().get(i));
                if (prescriptionView != null) {
                    binding.flAnswer.addView(prescriptionView);
                }
            }
        }


    }

    /**
     * 描述性回答
     *
     * @param binding
     * @param answer
     */
    @SuppressWarnings("unchecked")
    protected void setFill(final ItemAnswerBinding binding, final Answer answer) {
        //填写文本
        ItemEditBinding editBinding = ItemEditBinding.inflate(getInflater(), binding.flAnswer, true);
        editBinding.etAnswer.setTextColor(Color.parseColor("#363636"));
        editBinding.etAnswer.setBackgroundResource(R.drawable.shape_edit);

        if (answer.getAnswerContent() instanceof List) {
            List<String> content = (List<String>) answer.getAnswerContent();
            if (!content.isEmpty()) {
                editBinding.etAnswer.setText(content.get(0));
            }
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

                if (isEditMode) {
                    imageBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //删除图片
                            needPillsOrImages = position;
                            TwoChoiceDialog.show(mActivity, "是否删除图片?", "取消", "确定", new TwoChoiceDialog.Options() {
                                @Override
                                public void onApplyClick(TwoChoiceDialog dialog) {
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
                                public void onCancelClick(TwoChoiceDialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                            return true;
                        }
                    });
                }
                imageBinding.setData(answer.getImageUrls().get(i));
            }
        }
        //上传图片button
        if (isEditMode) {
            ItemImageBinding uploadBinding = ItemImageBinding.inflate(getInflater(), flowLayout, true);
            uploadBinding.practitionerImg.setBackgroundResource(R.drawable.ic_upload);

            uploadBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    needPillsOrImages = position;
                    new PickImageDialog(mActivity, Constants.UPLOAD_REQUEST_CODE).show();
                }
            });
        }
    }

    protected View getPrescriptionView(final ItemAnswerBinding binding, final Answer answer, final Prescription data) {
        final ItemPrescriptionBinding prescriptionBinding = ItemPrescriptionBinding.inflate(LayoutInflater.from(getContext()),
                binding.flAnswer, false);
        prescriptionBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.flAnswer.removeView(prescriptionBinding.getRoot());
                List<Prescription> prescriptions = answer.getPrescriptions();
                if (prescriptions.contains(data)) {
                    prescriptions.remove(data);
                    //用药信息为空
                    if (prescriptions.size() == 0) {
                        clearPositionFill(binding, answer);
                    }
                    answer.setPrescriptions(prescriptions);
                    set(binding.getVh().getAdapterPosition(), answer);
                    notifyItemChanged(binding.getVh().getAdapterPosition());
                }
            }
        });
        prescriptionBinding.setData(data);
        return prescriptionBinding.getRoot();
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
            if (getItemViewType(i) == R.layout.item_further_consultation) {
                Object addOn = saveFurtherConsultation(i);
                if (addOn != null) {
                    answerList.put(((FurtherConsultationVM) get(i)).getQuestionId() + "", addOn);
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
            case Question.TYPE_PILLS: {
                return savePills(answer);
            }
            case Question.TYPE_FILL: {
                return saveFill(answer);
            }
            case Question.TYPE_UPLOADS: {
                return saveUpload(answer);
            }
            case Question.TYPE_SEL:
            case Question.TYPE_CHECKBOX: {
                return saveButton(answer);
            }
            case Question.TYPE_RADIO: {
                return saveButton(answer);
            }
            case Question.TYPE_TIME: {
                return saveTime(answer, position);
            }
            case Question.TYPE_DROP_DOWN: {
                return saveDropDown(answer, position);
            }
            default: {
                break;
            }
        }
        return null;
    }

    private Object saveFurtherConsultation(int position) {
        LayoutId layoutId = get(position);
        if (layoutId.getItemLayoutId() == R.layout.item_further_consultation) {
            FurtherConsultationVM item = (FurtherConsultationVM) layoutId;


            return item.toJsonAnswer();
        } else {
            return null;
        }
    }

    private Object saveDropDown(Answer answer, int position) {
        LayoutId layoutId = get(position + 1);
        if (layoutId.getItemLayoutId() == R.layout.item_hospital) {
            ItemSelectHospital item = (ItemSelectHospital) layoutId;


            return item.toJsonAnswer();
        } else {
            return null;
        }
    }

    private Object saveTime(Answer answer, int position) {
        LayoutId layoutId = get(position + 1);
        if (layoutId instanceof ItemPickDate) {
            ItemPickDate item = (ItemPickDate) layoutId;
            HashMap<String, Object> result = new HashMap<>();
            result.put("type", new String[]{});
            String date = item.getDate();
            String[] dateList = new String[]{date};
            result.put("content", dateList);

            return result;
        } else {
            return null;
        }
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
        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();
        for (com.doctor.sun.entity.Options options : answer.getQuestion().getOptions()) {
            String s = answer.getSelectedOptions().get(options.getOptionType());
            if (s != null) {
                type.add(options.getOptionType());
                String optionInput = options.getOptionInput();
                if (optionInput == null) {
                    content.add(s);
                } else {
                    content.add(s.replace("{fill}", optionInput));
                }
            }
        }
        boxAnswer.put("type", type);
        boxAnswer.put("content", content);
        boxAnswer.put("mark", "0");
        return boxAnswer;
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

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    @Override
    public void clear() {
        super.clear();
    }
}
