package com.example.peernow360.dto;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class InvitationDto {

    private String user_id;
    private String owner_image;
    private String owner_id;
    private String owner_name;
    private int project_number;
    private String project_title;
    private String role;
    private String start_date;
    private String end_date;
    private String status;
}
