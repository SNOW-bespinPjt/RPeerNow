package com.example.peernow360.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserEnum {

    /*
     * UserMemberService
     */

    INSERT_ACCOUNT_AT_DB_FAIL(0),
    INSERT_ACCOUNT_AT_DB_SUCCESS(1),
    DB_ERROR(-1),
    ID_IS_ALREADY_EXIST(-2);
//    public static final int ID_IS_ALREADY_EXIST = -2;

    //enum과 constatnt 비교 글을 읽고 enum으로 바꾸기!

    private final int userEnum;

}
