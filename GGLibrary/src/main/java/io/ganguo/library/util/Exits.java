package io.ganguo.library.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

import io.ganguo.library.AppManager;
import io.ganguo.library.BaseApp;
import io.ganguo.library.R;
import io.ganguo.library.common.ToastHelper;

/**
 * 退出快捷工具
 * <p/>
 * Created by tony on 9/28/14.
 */
public class Exits {
    private static boolean isExit = false;

    public static void exitByDialog(final Activity activity) {
        new AlertDialog
                .Builder(activity)
                .setTitle(R.string.exit_alert)
                .setMessage(R.string.exit_message)
                .setNegativeButton(R.string.exit_cancel, null)
                .setPositiveButton(R.string.exit_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                })
                .create()
                .show();
    }

    /**
     * 双击退出函数
     *
     * @param context
     */
    public static void exitByDoublePressed(BaseApp context) {
        if (isExit == false) {
            isExit = true; // 准备退出
            ToastHelper.showMessage(context, R.string.exit_press_message);
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            exit();
        }
    }

    /**
     * 退出
     */
    public static void exit() {
        AppManager.exitApp();
    }

    /**
     * 跳转到主屏幕
     *
     * @param activity
     */
    public static void toHome(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }
}
