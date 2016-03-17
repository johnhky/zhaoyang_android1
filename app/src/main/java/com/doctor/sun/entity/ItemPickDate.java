package com.doctor.sun.entity;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.PickDateDialog;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rick on 12/22/15.
 */
public class ItemPickDate extends BaseItem {
    private final GregorianCalendar calendar = new GregorianCalendar();
    private String title;

    public void setType(String type) {
        this.type = type;
    }

    private String type;


    public ItemPickDate(int layoutId, String title) {
        super(layoutId);
        this.title = title;
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int dayOfMonth;
    private int monthOfYear;
    private int year;
    DatePickerDialog.OnDateSetListener setBeginDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ItemPickDate.this.year = year;
            ItemPickDate.this.monthOfYear = monthOfYear;
            ItemPickDate.this.dayOfMonth = dayOfMonth;

            notifyChange();
        }
    };


    public int getItemLayoutId() {
        return R.layout.item_pick_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

    public String getDate() {
        return String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
    }

    public String getBirthMonth() {
        return String.format("%04d-%02d", year, monthOfYear + 1);
    }

    public String getBirthday() {
        return getDate();
    }

    public String getTime() {
        return getDate();
    }

    public void setDate(String date) {
        String[] split = date.split("-");
        year = Integer.valueOf(split[0]);
        monthOfYear = Integer.valueOf(split[1]);
        dayOfMonth = Integer.valueOf(split[2]);
    }


    public OnItemClickListener pickTime() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), setBeginDate, year, monthOfYear, dayOfMonth);
                datePickerDialog.show();
            }
        };
    }

    public OnItemClickListener pickTime2() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {

                final PickDateDialog dateDialog = new PickDateDialog(view.getContext(), type);
                dateDialog.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
                    @Override
                    public boolean onCellClicked(Date date) {
                        boolean result = dateDialog.isContains(date);
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        if (result) {
                            ItemPickDate.this.year = calendar.get(Calendar.YEAR);
                            ItemPickDate.this.monthOfYear = calendar.get(Calendar.MONTH);
                            ItemPickDate.this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                            notifyChange();
                        }
                        dateDialog.dismiss();
                        return true;
                    }
                });
                dateDialog.show();
            }
        };
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
}
