package com.doctor.sun.vm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

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

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 12/22/15.
 */
public class ItemPickDate extends BaseItem {
    public static final String TAG = ItemPickDate.class.getSimpleName();
    public static final long ONE_HUNDRED_YEAR = 3153600000000L;
    public static final long ONE_DAY_MILLIS = 86400000L;

    private final GregorianCalendar calendar = new GregorianCalendar();
    private String title;

    private int subPosition;

    private int type;
    private int dayOfMonth;
    private int monthOfYear;
    private int year;

    public boolean isAnswered = true;

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


    private DatePickerDialog.OnDateSetListener setBeginDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ItemPickDate.this.year = year;
            ItemPickDate.this.monthOfYear = monthOfYear;
            ItemPickDate.this.dayOfMonth = dayOfMonth;
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            notifyChange();
        }
    };


    public int getSubPosition() {
        return subPosition;
    }

    public void setSubPosition(int subPosition) {
        this.subPosition = subPosition;
    }

    public void setType(int type) {
        this.type = type;
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
    }

    //TODO
    public void setDateAndNotify(String date) {
        setDate(date);
        notifyChange();
    }

    public void pickTime(Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, setBeginDate, year, monthOfYear, dayOfMonth);
        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.show();
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datePicker.setMaxDate(System.currentTimeMillis());
                datePicker.setMinDate(System.currentTimeMillis() - ONE_HUNDRED_YEAR);
            }
        }, 100);
        isAnswered = true;
    }

    public void pickTime(Context context, final long minDate, final long maxDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, setBeginDate, year, monthOfYear, dayOfMonth);
        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.show();
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datePicker.setMaxDate(maxDate);
                datePicker.setMinDate(minDate);
            }
        }, 100);
        isAnswered = true;
    }

    public void pickFutureTime(Context context, final int dayRangeBeforeNow, final int dayRangeFromNow) {
        final long passMillis = (long) dayRangeBeforeNow * ONE_DAY_MILLIS;
        final long futureAmount = (long) dayRangeFromNow * ONE_DAY_MILLIS;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, setBeginDate, year, monthOfYear, dayOfMonth + 1);
        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.show();
        //有些机子,looper机制可能不一样,post runnable没有放到最后才运行,所以延迟100毫秒
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datePicker.setMaxDate(System.currentTimeMillis() + futureAmount);
                datePicker.setMinDate(System.currentTimeMillis() - passMillis + ONE_DAY_MILLIS);
            }
        }, 100);
        isAnswered = true;
    }

    public void pickTime2(Context context) {
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
        isAnswered = true;
    }

    public long getMillis() {
        return calendar.getTimeInMillis();
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (!isAnswered) {
            return null;
        }
        if (!isEnabled()) {
            return null;
        }
        if (getItemLayoutId() == R.layout.item_reminder2) {
            return null;
        }

        if (getDate() == null || getDate().equals("")) {
            return null;
        }
        HashMap<String, Object> result = new HashMap<>();
        String key = getKey().replace(QuestionType.sDate, "");
        Questions2 item = (Questions2) adapter.get(key);
        String questionId = item.answerId;

        result.put("question_id", questionId);
        result.put("fill_content", getDate());
        return result;
    }

    @Override
    public String getValue() {
        return getBirthMonth();
    }
}