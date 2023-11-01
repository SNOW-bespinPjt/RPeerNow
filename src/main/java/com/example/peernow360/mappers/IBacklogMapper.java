package com.example.peernow360.mappers;

import com.example.peernow360.dto.BacklogDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface IBacklogMapper {
    public int createBacklogInfo(BacklogDto backlogDto);

    public void insertBacklogFile(Map<String, Object> map);

}
