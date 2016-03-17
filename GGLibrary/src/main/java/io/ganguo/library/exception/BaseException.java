package io.ganguo.library.exception;

import io.ganguo.library.BaseApp;

/**
 * Exception - 基类
 * <p/>
 * Created by Tony on 9/30/15.
 */
public abstract class BaseException extends Exception {

    private int messageResId;

    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public BaseException() {
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public BaseException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage the detail message for this exception.
     * @param throwable
     */
    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified cause.
     *
     * @param throwable the cause of this exception.
     */
    public BaseException(Throwable throwable) {
        super(throwable);
    }

    public int getMessageResId() {
        return messageResId;
    }

    public void setMessageResId(int messageResId) {
        this.messageResId = messageResId;
    }

    @Override
    public String getMessage() {
        if (messageResId != 0) {
            return BaseApp.me().getResources().getString(messageResId);
        }
        return super.getMessage();
    }
}
