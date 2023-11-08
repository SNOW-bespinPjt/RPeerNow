package com.example.peernow360.dto;

import lombok.Data;

@Data
public class BurnDownDto {

    private int no;
    private int ori_no;
    private int sprint_no;
    private int lapse;
    private int task;
    private int done_job;
    private int sub_day;
    private String start_date;
    private String end_date;
    private String reg_date;
    private String mod_date;

}
