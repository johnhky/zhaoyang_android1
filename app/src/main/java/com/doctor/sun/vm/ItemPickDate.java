package com.doctor.sun.vm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.doctor.sun.R;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.Calendar;
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
    public static final int DAYS_OF_ONE_HUNDRED_YEAR = 36525;

    private final GregorianCalendar calendar = new GregorianCalendar();
    private String title;

    private int subPosition;

    private int type;

    public boolean isAnswered = true;

    public ItemPickDate(int layoutId, String title) {
        super(layoutId);
        this.title = title;
        setYear(calendar.get(Calendar.YEAR) - 18);
    }

    public ItemPickDate(int layoutId, String title, int yearBefore) {
        super(layoutId);
        this.title = title;
        setYear(calendar.get(Calendar.YEAR) - yearBefore);
    }


    private DatePickerDialog.OnDateSetListener setBeginDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
        return String.format(Locale.CHINA, "%04d-%02d-%02d", getYear(), getMonthOfYear() + 1, getDayOfMonth());
    }

    public String getBirthMonth() {
        return String.format(Locale.CHINA, "%04d-%02d", getYear(), getMonthOfYear() + 1);
    }

    public String getToday() {
        return String.format(Locale.CHINA, "%04d-%02d-%02d", getYear() + 18, getMonthOfYear() + 1, getDayOfMonth());
    }

    public String getTomorrow() {
        return String.format(Locale.CHINA, "%04d-%02d-%02d", getYear(), getMonthOfYear() + 1, getDayOfMonth() + 1);
    }

    public String getBirthday() {
        return getDate();
    }

    public String getTime() {
        return getDate();
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setDayOfMonth(int dayOfMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public int getMonthOfYear() {
        return calendar.get(Calendar.MONTH);
    }

    public void setMonthOfYear(int monthOfYear) {
        calendar.set(Calendar.MONTH, monthOfYear);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public void setYear(int year) {
        calendar.set(Calendar.YEAR,year);
    }

    //TODO
    public void setDate(String date) {
        try {
            if (date == null || date.equals(""))
                return;
            String[] split = date.split("-");
            setYear(Integer.valueOf(split[0]));
            setMonthOfYear(Integer.valueOf(split[1]) - 1);
            setDayOfMonth(Integer.valueOf(split[2]));
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
        pickFutureTime(context, DAYS_OF_ONE_HUNDRED_YEAR, 0);
    }

    public void pickTime(Context context, final long minDate, final long maxDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, setBeginDate, getYear(), getMonthOfYear(), getDayOfMonth());
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
        long maxDate = System.currentTimeMillis() + futureAmount;
        long minDate = System.currentTimeMillis() - passMillis + ONE_DAY_MILLIS;
        pickTime(context, minDate, maxDate);

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
