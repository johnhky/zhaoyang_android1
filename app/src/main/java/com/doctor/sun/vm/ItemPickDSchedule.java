package com.doctor.sun.vm;

import android.content.Context;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.widget.PickDateDialog;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by rick on 12/22/15.
 */
public class ItemPickDSchedule extends BaseItem {
    public static final String TAG = ItemPickDSchedule.class.getSimpleName();

    private final GregorianCalendar calendar = new GregorianCalendar();
    private String title;

    private int subPosition;

    private int type;
    private int dayOfMonth;
    private int monthOfYear;
    private int year;

    public boolean isAnswered = false;

    public ItemPickDSchedule(int layoutId, String title) {
        super(layoutId);
        this.title = title;
        year = calendar.get(Calendar.YEAR) - 18;
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public ItemPickDSchedule(int layoutId, String title, int yearBefore) {
        super(layoutId);
        this.title = title;
        year = calendar.get(Calendar.YEAR) - yearBefore;
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }


    public int getSubPosition() {
        return subPosition;
    }

    public void setSubPosition(int subPosition) {
        this.subPosition = subPosition;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

    public String getDate() {
        if (!isAnswered) {
            return "点击选择日期";
        }
        return String.format(Locale.CHINA, "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
    }

    public String getBirthMonth() {
        return String.format(Locale.CHINA, "%04d-%02d", year, monthOfYear + 1);
    }

    public String getToday() {
        return String.format(Locale.CHINA, "%04d-%02d-%02d", year + 18, monthOfYear + 1, dayOfMonth);
    }

    public String getTomorrow() {
        return String.format(Locale.CHINA, "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth + 1);
    }

    public String getBirthday() {
        return getDate();
    }

    public String getTime() {
        return getDate();
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //TODO
    public void setDate(String date) {
        try {
            if (date == null || date.equals("")) return;
            String[] split = date.split("-");
            year = Integer.valueOf(split[0]);
            monthOfYear = Integer.valueOf(split[1]) - 1;
            dayOfMonth = Integer.valueOf(split[2]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        notifyChange();
    }

    //TODO
    public void setDateAndNotify(String date) {
        setDate(date);
        notifyChange();
    }


    public void pickDSchedule(Context context) {
        pickDateImpl(context, type);
    }

    void pickDateImpl(Context context, int type) {
        final PickDateDialog dateDialog = new PickDateDialog(context, type);
        dateDialog.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                boolean result = dateDialog.isContains(date);
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                if (result) {
                    ItemPickDSchedule.this.year = calendar.get(Calendar.YEAR);
                    ItemPickDSchedule.this.monthOfYear = calendar.get(Calendar.MONTH);
                    ItemPickDSchedule.this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    notifyChange();
                }
                dateDialog.dismiss();
                return true;
            }
        });
        dateDialog.show();
        isAnswered = true;
    }


    @Override
    public String getValue() {
        return getBirthMonth();
    }
}
