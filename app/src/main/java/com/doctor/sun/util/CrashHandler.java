//package com.doctor.sun.util;
//
//import android.content.Context;
//import android.os.Looper;
//
//import com.doctor.sun.dto.ApiDTO;
//import com.doctor.sun.http.Api;
//import com.doctor.sun.module.ToolModule;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class CrashHandler implements Thread.UncaughtExceptionHandler {
//    public static final String TAG = "CrashHandler";
//    private static CrashHandler INSTANCE = new CrashHandler();
//    private ToolModule api = Api.of(ToolModule.class);
//
//    private CrashHandler() {
//    }
//
//    public static CrashHandler getInstance() {
//        return INSTANCE;
//    }
//
//    public void init(Context ctx) {
//        Thread.setDefaultUncaughtExceptionHandler(this);
//    }
//
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        // if (!handleException(ex) && mDefaultHandler != null) {
//        // mDefaultHandler.uncaughtException(thread, ex);
//        // } else {
//        // android.os.Process.killProcess(android.os.Process.myPid());
//        // System.exit(10);
//        // }
//        handleException(ex);
//    }
//
//    /**
//     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
//     *
//     * @param ex
//     * @return true:如果处理了该异常信息;否则返回false
//     */
//    private boolean handleException(Throwable ex) {
//        if (ex == null) {
//            return true;
//        }
//        // new Handler(Looper.getMainLooper()).post(new Runnable() {
//        // @Override
//        // public void run() {
//        // new AlertDialog.Builder(mContext).setTitle("提示")
//        // .setMessage("程序崩溃了...").setNeutralButton("我知道了", null)
//        // .create().show();
//        // }
//        // });
//
//        api.crashLog(JacksonUtils.toJson(ex.toString())).enqueue(new Callback<ApiDTO<Void>>() {
//            @Override
//            public void onResponse(Call<ApiDTO<Void>> call, Response<ApiDTO<Void>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ApiDTO<Void>> call, Throwable t) {
//
//            }
//        });
//        return true;
//    }
//}