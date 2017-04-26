package com.goldentwo.exception;

public class MatchException extends RuntimeException {
    public MatchException() {
        super();
    }

    public MatchException(String s) {
        super(s);
    }

    public MatchException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MatchException(Throwable throwable) {
        super(throwable);
    }

    protected MatchException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
