package com.sparta.community.exception;

import org.springframework.dao.DataAccessException;

public class CustomErrorException extends DataAccessException {
    public CustomErrorException(String msg) {
        super(msg);
    }
}
