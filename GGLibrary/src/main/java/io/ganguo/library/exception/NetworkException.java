package io.ganguo.library.exception;

import com.google.gson.JsonSyntaxException;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.ganguo.library.BaseApp;
import io.ganguo.library.R;
import io.ganguo.library.util.Networks;

/**
 * network exception.
 * <p/>
 * Created by Tony on 10/6/15.
 */
public class NetworkException extends BaseException {

    public NetworkException(Throwable e) {
        super(e);

        // check exception
        checkMessage(e);
    }

    /**
     * 验证错误类型消息
     *
     * @param e
     */
    private void checkMessage(Throwable e) {

        if (!Networks.isConnected(BaseApp.me())) {
            setMessageResId(R.string.ex_network_disconnected);
        } else if (e instanceof UnknownHostException) {
            setMessageResId(R.string.ex_network_unknown_host);
        } else if (e instanceof InterruptedIOException) {
            // SocketTimeoutException
            setMessageResId(R.string.ex_network_timeout);
        } else if (e instanceof SocketException) {
            // ConnectException
            setMessageResId(R.string.ex_network_socket);
        } else if (e instanceof JsonSyntaxException) {
            // gson convert
            setMessageResId(R.string.ex_network_convert);
        } else {
            setMessageResId(R.string.ex_network_default);
        }
    }

}
