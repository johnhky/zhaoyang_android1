package com.doctor.sun.http.callback;

import android.accounts.AuthenticatorException;
import android.accounts.NetworkErrorException;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.event.OnTokenExpireEvent;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rick on 11/27/15.
 */
public abstract class ApiCallback<T> implements Callback<ApiDTO<T>> {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private int retryTime = 0;


    @Override
    final public void onResponse(Call<ApiDTO<T>> call, Response<ApiDTO<T>> response) {
        if (response.body() == null) {
            onFailure(call, new NullPointerException());
            return;
        }
        int code = Integer.parseInt(response.body().getStatus());
        if (code < 300) {
            T data = response.body().getData();
            if (data != null) {
                handleBody(response.body());
            } else {
                handleApi(response.body());
            }
        } else if (code == 401) {
            onFailure(call, new AuthenticatorException("not login"));
            int passFirstTime = Config.getInt(Constants.PASSFIRSTTIME, -1);
            long lastTime = Config.getLong(LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), -1);
            Config.clearAll();
            Config.putLong(LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), lastTime);
            Config.putInt(Constants.PASSFIRSTTIME, passFirstTime);
            EventHub.post(new OnTokenExpireEvent());
        } else {
            String msg = response.body().getMessage();
            Toast.makeText(AppContext.me(), msg, Toast.LENGTH_SHORT).show();
            onFailure(call, new NetworkErrorException());
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

    protected void handleBody(ApiDTO<T> body) {
        handleResponse(body.getData());
    }

    protected abstract void handleResponse(T response);

    @Override
    public void onFailure(Call<ApiDTO<T>> call, Throwable t) {
        t.printStackTrace();
        if (t instanceof UnknownHostException) {
            Toast.makeText(AppContext.me(), "无法连接服务器,请检查您的网络连接", Toast.LENGTH_SHORT).show();
        } else if (t instanceof SocketTimeoutException) {
            if (retryTime == 0) {
                retryTime += 1;
                call.clone().enqueue(this);
            }
        }
    }
}
