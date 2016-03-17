package io.ganguo.library.util.date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tony on 1/4/15.
 */
public class DateUI {

    /**
     * 选择数字
     *
     * @param context
     * @param text
     * @param min
     * @param max
     */
    public static void numberPicker(Context context, final EditText text, int min, int max) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        int position = 0;
        final String[] items = new String[max - min + 1];
        for (int i = 0; i < items.length; i++) {
            String value = String.valueOf(i + 30);
            items[i] = value;
            if (text.getText().toString().equals(value)) {
                position = i;
            }
        }
        builder.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text.setText(items[which]);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * @param context
     * @param date    文本编辑器，规定日期格式：yyyy-MM-dd
     */
    public static void datePicker(Context context, final EditText date) {
        String[] dateArray = null;
        if (date.getText() != null && !date.getText().toString().equals("")) {
            dateArray = date.getText().toString().split("-");
        } else {
            dateArray = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = new DatePicker(context);
        datePicker.setCalendarViewShown(true);
        // datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.init(Integer.valueOf(dateArray[0]), Integer.valueOf(dateArray[1]) - 1, Integer.valueOf(dateArray[2]), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker picker, int year, int month, int day) {
                onDateConfirmed(year, month, day, date);
            }
        });
        builder.setView(datePicker);
        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDateConfirmed(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), date);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void onDateConfirmed(int year, int month, int day, TextView date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date.setText(sdf.format(calendar.getTime()));
    }


    /**
     * @param context
     * @param date    文本编辑器，规定日期格式：yyyy-MM-dd
     */
    public static void datePicker(Context context, final TextView date) {
        String[] dateArray = null;
        if (date.getText() != null && !date.getText().toString().equals("")) {
            dateArray = date.getText().toString().split("-");
        } else {
            dateArray = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = new DatePicker(context);
        datePicker.setCalendarViewShown(true);
        // datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.init(Integer.valueOf(dateArray[0]), Integer.valueOf(dateArray[1]) - 1, Integer.valueOf(dateArray[2]), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker picker, int year, int month, int day) {
                onDateConfirmed(year, month, day, date);
            }
        });
        builder.setView(datePicker);
        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDateConfirmed(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), date);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
