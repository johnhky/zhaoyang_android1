package com.doctor.sun.util;

import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

import java.util.HashSet;

/**
 * Created by rick on 2/7/2016.
 */
public class OptionsUtil {
    public static final HashSet<Integer> isCheckInEnabledQuestion = new HashSet<>();
    public static final HashSet<Integer> acceptSurveyEnabledQuestion = new HashSet<>();
    public static final HashSet<Integer> refuseSurveyDisabledQuestion = new HashSet<>();

    static {
        isCheckInEnabledQuestion.add(22);
        isCheckInEnabledQuestion.add(24);

        refuseSurveyDisabledQuestion.add(29);
        refuseSurveyDisabledQuestion.add(48);

        acceptSurveyEnabledQuestion.add(22);
        acceptSurveyEnabledQuestion.add(29);
        acceptSurveyEnabledQuestion.add(28);
        acceptSurveyEnabledQuestion.add(47);
        acceptSurveyEnabledQuestion.add(48);


    }

    public static boolean isEnable(final BaseAdapter adapter, final int adapterPosition) {
        Answer parent = getParent(adapter, adapterPosition);
        int questionId = parent.getQuestionId();
        return isQuestionEnable(adapter, questionId);
    }

    public static boolean isEnable(final BaseAdapter adapter, final BaseViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        Options options = (Options) adapter.get(adapterPosition);
        Answer parent = getParent(adapter, options.getParentPosition());
        int questionId = parent.getQuestionId();
        return isQuestionEnable(adapter, questionId);
    }

    public static boolean isQuestionEnable(BaseAdapter adapter, int questionId) {
        if (isSelected(adapter, 3)) {
//            3=(22 B)  -ALL +24
            return isCheckInEnabledQuestion.contains(questionId);
        }

        if (isSelected(adapter, 15)) {
            if (acceptSurveyEnabledQuestion.contains(questionId)) {
                return true;
            }
            return false;
        }
        if (isSelected(adapter, 14)) {
            if (refuseSurveyDisabledQuestion.contains(questionId)) {
                return false;
            }
        }
        return true;
    }

    public static Answer getParent(BaseAdapter adapter, int position) {
        return (Answer) adapter.get(position);
    }

    public boolean isSelected(final BaseAdapter adapter, final BaseViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        return isSelected(adapter, adapterPosition);
    }

    public static boolean isSelected(BaseAdapter adapter, int adapterPosition) {
        Options options = (Options) adapter.get(adapterPosition);
        Answer answer = getParent(adapter, options.getParentPosition());
        return answer.getSelectedOptions().containsKey(options.getOptionType());
    }
}
