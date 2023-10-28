package com.example.peernow360.dto;

import com.example.peernow360.response.CommonResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
