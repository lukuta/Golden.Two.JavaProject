package com.goldentwo.exception;

public class TournamentException extends RuntimeException {
    public TournamentException() {
        super();
    }

    public TournamentException(String message) {
        super(message);
    }

    public TournamentException(String message, Throwable cause) {
        super(message, cause);
    }

    public TournamentException(Throwable cause) {
        super(cause);
    }

    protected TournamentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}