package com.doctor.sun.vo;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 18/6/2016.
 */
public class ItemReminderList extends BaseItem {

    private int questionId;

    private SimpleAdapter<ItemPickDate, ViewDataBinding> simpleAdapter;
    private String itemId;

    public SimpleAdapter adapter(Context context) {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter<>(context);
            simpleAdapter.mapLayout(R.layout.item_pick_date, R.layout.item_reminder);
            simpleAdapter.onFinishLoadMore(true);
        }
        return simpleAdapter;
    }

    public void addReminder() {
        if (simpleAdapter != null) {
            ItemPickDate object = new ItemPickDate(R.layout.item_pick_date, "", 0);
            object.setPosition(simpleAdapter.size() + 1);
            simpleAdapter.add(object);
            simpleAdapter.notifyItemInserted(simpleAdapter.size() - 1);
        }
    }

    public void addReminder(Reminder reminder) {
        if (reminder == null) {
            return;
        }
        if (simpleAdapter != null) {
            ItemPickDate object = new ItemPickDate(R.layout.item_pick_date, reminder.content, 0);
            object.setPosition(simpleAdapter.size() + 1);
            object.setDate(reminder.time);
            simpleAdapter.add(object);
            simpleAdapter.notifyItemInserted(simpleAdapter.size());
        }
    }

    public void addReminders(List<Reminder> reminderList) {
        if (reminderList == null) {
            return;
        }
        for (Reminder reminder : reminderList) {
            addReminder(reminder);
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_reminder_list;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public HashMap<String, Object> toJsonAnswer() {
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<String> type = new ArrayList<>();
        ArrayList<Reminder> content = getReminders();

        result.put("type", type);
        result.put("content", content);

        return result;
    }

    @NonNull
    public ArrayList<Reminder> getReminders() {
        ArrayList<Reminder> content = new ArrayList<>();
        for (int i = 0; i < simpleAdapter.size(); i++) {
            ItemPickDate itemPickDate = simpleAdapter.get(i);
            String title = itemPickDate.getTitle();
            if (title != null && !title.equals("")) {
                content.add(new Reminder(itemPickDate.getTime(), title));
            }
        }
        return content;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_reminder_list;
    }

    @Override
    public long getCreated() {
        return -getPosition();
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getKey() {
        return itemId;
    }
}
