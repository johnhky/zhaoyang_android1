package com.doctor.sun.http.callback;

import android.accounts.AuthenticatorException;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.event.OnTokenExpireEvent;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rick on 11/27/15.
 */
public abstract class ApiCallback<T> implements Callback<ApiDTO<T>> {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";

    @Override
    final public void onResponse(Response<ApiDTO<T>> response, Retrofit retrofit) {
        int code = Integer.parseInt(response.body().getStatus());
        Log.e("onResponse", "response code: " + code);
        if (code < 300) {
            T data = response.body().getData();
            if (data != null) {
                handleResponse(data);
            } else {
                handleApi(response.body());
            }
        } else if (code == 401) {
            onFailure(new AuthenticatorException("not login"));
            int passFirstTime = Config.getInt(Constants.PASSFIRSTTIME, -1);
            long lastTime = Config.getLong(LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), -1);
            Config.clearAll();
            Config.putLong(LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), lastTime);
            Config.putInt(Constants.PASSFIRSTTIME,passFirstTime);
            EventHub.post(new OnTokenExpireEvent());
        } else {
            String status = response.body().getStatus();
            String msg = response.body().getMessage();
            if (null != status) {
                Log.e("onResponse", "status: " + status + "  msg:" + msg);
                onFailure(new IllegalArgumentException(msg));
                ToastError(status);
            }
        }
    }

    private void ToastError(String status) {
        String msg = ErrorMap.getInstance().get(status);
        if (msg != null) {
            Toast.makeText(AppContext.me(), msg, Toast.LENGTH_LONG).show();
        }
    }

    protected void handleApi(ApiDTO<T> body) {
    }


    protected abstract void handleResponse(T response);

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
    }
}
