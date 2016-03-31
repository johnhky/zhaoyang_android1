package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemAnswerBinding;
import com.doctor.sun.databinding.ItemFillAnswerBinding;
import com.doctor.sun.databinding.ItemImageBinding;
import com.doctor.sun.databinding.ItemPillsBinding;
import com.doctor.sun.databinding.ItemTickBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.widget.FlowLayout;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;

/**
 * 填写问卷 只读 adapter
 * <p/>
 * Created by rick on 11/24/15.
 */
public class AnswerAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private QuestionModule api = Api.of(QuestionModule.class);
    private Appointment appointment;
    private int positionMargin = 0;

    public AnswerAdapter(Context context, Appointment appointment) {
        super(context);
        this.appointment = appointment;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, int position) {
        vh.getBinding().setVariable(BR.position, String.valueOf(position + positionMargin));
        if (vh.getItemViewType() == R.layout.item_answer) {
            final ItemAnswerBinding binding = (ItemAnswerBinding) vh.getBinding();
            final Answer answer = (Answer) get(position);

            bindContent(binding, answer);

            int userType = Config.getInt(Constants.USER_TYPE, -1);
            if (userType == AuthModule.PATIENT_TYPE || appointment.getIsFinish() == 1) {
                binding.flReset.setVisibility(View.GONE);
            } else {
                binding.flReset.setVisibility(View.VISIBLE);
                binding.flReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> need_fill = new ArrayList<>();
                        need_fill.add(answer.getId() + "");
                        api.refill(answer.getAppointmentId() + "",
                                need_fill).enqueue(new ApiCallback<List<Answer>>() {
                            @Override
                            protected void handleResponse(List<Answer> response) {
                                ToastHelper.showMessage(getContext(), "保存重填数据成功");
                                binding.getData().setNeedRefill(1);
                                binding.ivPosition.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bg_msg_count));
                            }

                            @Override
                            protected void handleApi(ApiDTO<List<Answer>> body) {
                                ToastHelper.showMessage(getContext(), "保存重填数据失败, 请检查网络设置");
                                super.handleApi(body);
                            }
                        });
                    }
                });
            }

            if (answer.getNeedRefill() == 1 && appointment.getIsFinish() != 1) {
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

    private void bindContent(ItemAnswerBinding binding, Answer answer) {
        binding.flAnswer.removeAllViews();

        switch (answer.getQuestion().getQuestionType()) {
            case "fills": {
                bindPills(binding, answer);
                break;
            }
            case "fill": {
                bindFill(binding, answer);
                break;
            }
            case "uploads": {
                bindImages(binding, answer);
                break;
            }
            case "checkbox": {
                bindAnswer(binding, answer);
                break;
            }
            case "radio": {
                bindAnswer(binding, answer);
                break;
            }
            default: {
                bindAnswer(binding, answer);
                break;
            }
        }
    }

    private void bindFill(ItemAnswerBinding binding, Answer answer) {
        if (answer.getAnswerContent() instanceof ArrayList) {
            try {
                List<Object> content = (List<Object>) answer.getAnswerContent();
                for (int i = 0; i < content.size(); i++) {
                    ItemFillAnswerBinding tickBinding = ItemFillAnswerBinding.inflate(getInflater(), binding.flAnswer, true);
                    tickBinding.setData(content.get(i).toString());
                }
            } catch (Exception e) {


            }
        }
    }

    private void bindPills(ItemAnswerBinding binding, Answer answer) {
        binding.flAnswer.setVisibility(View.VISIBLE);
        Object answerContent = answer.getAnswerContent();
        if (!(answerContent instanceof List)) return;
        List<Object> content = (List<Object>) answerContent;
        for (int i = 0; i < content.size(); i++) {
            ItemPillsBinding prescriptionBinding = ItemPillsBinding.inflate(getInflater(), binding.flAnswer, true);
            Prescription data = null;
            try {
                data = JacksonUtils.fromMap((LinkedHashMap) content.get(i), Prescription.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            prescriptionBinding.setData(data);

        }
    }

    private void bindAnswer(ItemAnswerBinding binding, Answer answer) {
        Options fillOptions = null;
        for (int i = answer.getQuestion().getOptions().size() - 1; i >= 0; i--) {
            Options options = answer.getQuestion().getOptions().get(i);
            if (options.getOptionContent().contains("{fill}")) {
                fillOptions = options;
                break;
            }
        }
        if (answer.getAnswerContent() instanceof ArrayList) {
            try {
                List<Object> type = (List<Object>) answer.getAnswerType();
                List<Object> content = (List<Object>) answer.getAnswerContent();
                for (int i = 0; i < type.size(); i++) {

                    String data = type.get(i).toString() + ". " + content.get(i).toString();

                    ItemTickBinding tickBinding = ItemTickBinding.inflate(getInflater(), binding.flAnswer, true);

                    if (fillOptions != null) {
                        if (fillOptions.getOptionType().equals(type.get(i).toString())) {
                            data = type.get(i).toString() + ". " +
                                    fillOptions.getOptionContent().replace("{fill}", content.get(i).toString());
                            tickBinding.setData(data);
                            tickBinding.tvTick.setActivated(true);
                        } else {
                        }
                    }
                    tickBinding.setData(data);
                    tickBinding.tvTick.setSelected(true);
                }
            } catch (Exception e) {


            }
        }
    }

    private void bindImages(ItemAnswerBinding binding, final Answer answer) {
        FlowLayout flowLayout = new FlowLayout(getContext());
        binding.flAnswer.addView(flowLayout, -1);
        if (answer.getAnswerContent() instanceof ArrayList) {
            try {
                List<Object> content = (List<Object>) answer.getAnswerContent();
                for (int i = 0; i < content.size(); i++) {
                    final String url = content.get(i).toString();
                    ItemImageBinding tickBinding = ItemImageBinding.inflate(getInflater(), flowLayout, true);
                    tickBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContext().startActivity(
                                    ImagePreviewActivity.makeIntent(getContext(), url));
                        }
                    });
                    tickBinding.setData(url);
                }
            } catch (Exception e) {

            }
        }
    }

    public int getPositionMargin() {
        return positionMargin;
    }

    public void setPositionMargin(int positionMargin) {
        this.positionMargin = positionMargin;
    }
}
