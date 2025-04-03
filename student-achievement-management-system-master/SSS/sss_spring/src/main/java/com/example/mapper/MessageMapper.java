package com.example.mapper;

import com.example.entity.Message;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper {

    int insert(Message message);

    void updateById(Message message);

    void deleteById(Integer id);

    @Select("SELECT * FROM `message` WHERE id = #{id}")
    Message selectById(Integer id);

    List<Message> selectAll(Message message);

    List<Message> selectConversation(String from, String to);
}
