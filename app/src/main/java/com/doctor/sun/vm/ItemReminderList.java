package com.doctor.sun.vm;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 18/6/2016.
 */
public class ItemReminderList extends BaseItem {

    private int questionId;

    private ArrayList<ItemPickDate> dates = new ArrayList<>();
    private SimpleAdapter<ItemPickDate, ViewDataBinding> simpleAdapter;
    private String itemId;
    private RecyclerView.AdapterDataObserver observer;


    public SimpleAdapter adapter(Context context) {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter<>();
            simpleAdapter.setData(dates);
            simpleAdapter.onFinishLoadMore(true);
            if (observer != null) {
                simpleAdapter.registerAdapterDataObserver(observer);
            }
        }
        return simpleAdapter;
    }

    public void addReminder() {
        if (dates != null) {
            ItemPickDate object = new ItemPickDate(R.layout.item_reminder, "", 0);
            object.setDayOfMonth(object.getDayOfMonth() + 1);
            object.setPosition(dates.size() + 1);
            dates.add(object);
            if (simpleAdapter != null) {
                simpleAdapter.notifyItemInserted(dates.size() - 1);
            }
        }
    }

    public void addReminder(Reminder reminder) {
        if (reminder == null) {
            return;
        }
        if (dates != null) {
            ItemPickDate object = new ItemPickDate(R.layout.item_reminder, reminder.content, 0);
            object.setPosition(dates.size() + 1);
            object.setDate(reminder.time);
            dates.add(object);
            if (simpleAdapter != null) {
                simpleAdapter.notifyItemInserted(dates.size());
            }
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

    public void addReminder(List<Map<String, String>> reminders) {
        for (int j = 0; j < reminders.size(); j++) {
            Reminder reminder = Reminder.fromMap(reminders.get(j));
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

    public int itemCount() {
        if (simpleAdapter == null) {
            return dates.size();
        } else {
            return simpleAdapter.size();
        }
    }

    @NonNull
    public ArrayList<Reminder> getReminders() {
        ArrayList<Reminder> content = new ArrayList<>();
        for (int i = 0; i < simpleAdapter.size(); i++) {
            ItemPickDate itemPickDate = simpleAdapter.get(i);
            String title = itemPickDate.getTitle();
            if (title != null && !title.equals("")) {
                content.add(new Reminder(itemPickDate.getDate(), title));
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

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (simpleAdapter.isEmpty()) {
            return null;
        } else {
            ArrayList<Object> arrayList = new ArrayList<>();
            for (int i = 0; i < simpleAdapter.size(); i++) {
                ItemPickDate itemPickDate = simpleAdapter.get(i);

                HashMap<String, String> object = new HashMap<>();
                object.put("time", itemPickDate.getDate());
                object.put("content", itemPickDate.getTitle());

                arrayList.add(object);
            }
            HashMap<String, Object> result = new HashMap<>();
            String key = getKey().replace(QuestionType.reminder, "");
            Questions2 item = (Questions2) adapter.get(key);
            result.put("question_id", item.answerId);
            result.put("fill_content", arrayList);
            return result;
        }
    }

    public void setChangeListener(RecyclerView.AdapterDataObserver observer) {
        this.observer = observer;
    }
}
