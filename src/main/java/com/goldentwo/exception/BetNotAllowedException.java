package com.goldentwo.exception;

public class BetNotAllowedException extends RuntimeException {
    public BetNotAllowedException() {
        super();
    }

    public BetNotAllowedException(String message) {
        super(message);
    }

    public BetNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BetNotAllowedException(Throwable cause) {
        super(cause);
    }

    protected BetNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
