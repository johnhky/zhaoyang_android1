package com.doctor.sun.vo;

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
import java.util.Locale;

/**
 * Created by rick on 12/22/15.
 */
public class ItemPickDate extends BaseItem {
    public static final long ONE_HUNDRED_YEAR = 3153600000000L;
    public static final long ONE_DAY_MILLIS = 86400000;

    private final GregorianCalendar calendar = new GregorianCalendar();
    private String title;

    public void setType(String type) {
        this.type = type;
    }

    private String type;


    public ItemPickDate(int layoutId, String title) {
        super(layoutId);
        this.title = title;
        year = calendar.get(Calendar.YEAR) - 18;
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public ItemPickDate(int layoutId, String title, int yearBefore) {
        super(layoutId);
        this.title = title;
        year = calendar.get(Calendar.YEAR) - yearBefore;
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
        return String.format(Locale.CHINA, "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
    }

    public String getBirthMonth() {
        return String.format(Locale.CHINA, "%04d-%02d", year, monthOfYear + 1);
    }

    public String getBirthday() {
        return getDate();
    }

    public String getTime() {
        return getDate();
    }

    //TODO
    public void setDate(String date) {
        String[] split = date.split("-");
        year = Integer.valueOf(split[0]);
        monthOfYear = Integer.valueOf(split[1]) - 1;
        dayOfMonth = Integer.valueOf(split[2]);
    }


    public View.OnClickListener pickTime() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), setBeginDate, year, monthOfYear, dayOfMonth);
                final DatePicker datePicker = datePickerDialog.getDatePicker();
                datePickerDialog.show();
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        datePicker.setMaxDate(System.currentTimeMillis());
                        datePicker.setMinDate(System.currentTimeMillis() - ONE_HUNDRED_YEAR);
                    }
                });
            }
        };
    }

    public View.OnClickListener pickFutureTime(final int dayRangeFromNow) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long futureAmount = (long) Math.abs(dayRangeFromNow) * ONE_DAY_MILLIS;
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), setBeginDate, year, monthOfYear, dayOfMonth);
                final DatePicker datePicker = datePickerDialog.getDatePicker();
                datePickerDialog.show();
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        datePicker.setMaxDate(System.currentTimeMillis() + futureAmount);
                        datePicker.setMinDate(System.currentTimeMillis());
                    }
                });
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

    public View.OnClickListener pickTime3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), setBeginDate, year, monthOfYear, dayOfMonth);
                final DatePicker datePicker = datePickerDialog.getDatePicker();
                datePickerDialog.show();
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
