package com.example.peernow360.dto;

import lombok.Data;

@Data
public class BacklogDto {

    private int no;
    private int project_no;
    private int sprint_no;
    private String user_id;
    private String title;
    private String detail;
    private String status;
    private String reg_date;
    private String mod_date;


}
