package com.example.peernow360.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class UserMemberDto {
    private String id;
    private String pw;
    private String name;
    private String mail;
    private String phone;
    private String team;
    private Object image;
    private String grade;
    private String reg_date;
    private String mod_date;
    private String role;
    private String status;


}