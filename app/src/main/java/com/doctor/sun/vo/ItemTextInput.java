package com.doctor.sun.vo;

import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.HashMap;

/**
 * Created by rick on 12/22/15.
 */
public class ItemTextInput extends BaseItem {


    private String title;
    private String input;

    public ItemTextInput(int itemLayoutId, String title) {
        super(itemLayoutId);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (input != null && !"".equals(input)) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("question_id", getKey().replace(QuestionType.fill, ""));
            result.put("fill_content", input);
            return result;
        } else {
            return null;
        }
    }
}
