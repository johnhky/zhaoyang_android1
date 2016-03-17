package io.ganguo.library.exception;

/**
 * Created by Tony on 9/30/15.
 */
public class BeansException extends BaseException {

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public BeansException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage the detail message for this exception.
     * @param throwable
     */
    public BeansException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
