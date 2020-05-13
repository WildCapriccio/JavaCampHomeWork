package com.lagou.edu.mvcframework.exceptions;

public class YuException extends ReflectiveOperationException {
    private static final long serialVersionUID = 6616958222490762000L;

    public YuException() {
        super();
    }

    public YuException(String s) {
        super(s);
    }
}
