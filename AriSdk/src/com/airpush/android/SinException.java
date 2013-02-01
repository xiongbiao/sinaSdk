package com.airpush.android;

public class SinException extends Exception {

    private static final long serialVersionUID = 1L;

    public SinException() {
        super();
    }

    public SinException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SinException(String detailMessage) {
        super(detailMessage);
    }

    public SinException(Throwable throwable) {
        super(throwable);
    }
    
}
