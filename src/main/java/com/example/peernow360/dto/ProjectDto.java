package com.example.peernow360.dto;

import com.example.peernow360.response.CommonResponse;
import lombok.Data;

@Data
public class ProjectDto {

    private int no;
    private String user_id;
    private String title;
    private String detail;
    private String start_date;
    private String end_date;
    private String reg_date;
    private String mod_date;
}
