package com.doctor.sun.http.callback;

import android.accounts.AuthenticatorException;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.QuestionStats;
import com.doctor.sun.event.OnTokenExpireEvent;
import com.doctor.sun.ui.adapter.AnswerAdapter;
import com.doctor.sun.ui.handler.QCategoryHandler;

import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rick on 2/2/2016.
 */
public class AnswerCallback implements Callback<ApiDTO<List<Answer>>> {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";

    private final AnswerAdapter adapter;
    private final QCategoryHandler data;

    public AnswerCallback(QCategoryHandler data, AnswerAdapter adapter) {
        this.adapter = adapter;
        this.data = data;
    }


    private void ToastError(String status) {
        String msg = ErrorMap.getInstance().get(status);
        if (msg != null) {
            Toast.makeText(AppContext.me(), msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(Response<ApiDTO<List<Answer>>> response, Retrofit retrofit) {
        int code = Integer.parseInt(response.body().getStatus());
        Log.e("onResponse", "response code: " + code);
        if (code < 300) {
            handleApi(response.body());
        } else if (code == 401) {
            onFailure(new AuthenticatorException("not login"));
            int passFirstTime = Config.getInt(Constants.PASSFIRSTTIME, -1);
            long lastTime = Config.getLong(LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), -1);
            Config.clearAll();
            Config.getLong(LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), lastTime);
            Config.putInt(Constants.PASSFIRSTTIME, passFirstTime);
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

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        getAdapter().onFinishLoadMore(true);
    }

    protected void handleApi(ApiDTO<List<Answer>> body) {
        QuestionStats count = body.getCount();
        if (count != null) {
            getAdapter().clear();
            count.setName(data.getCategoryName());
            getAdapter().add(count);
        }
        getAdapter().addAll(body.getData());
        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
    }

    public AnswerAdapter getAdapter() {
        return adapter;
    }
}
