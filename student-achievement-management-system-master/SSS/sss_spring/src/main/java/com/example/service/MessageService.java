//package com.example.service;
//
//import com.example.entity.Message;
//import com.example.mapper.MessageMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class MessageService {
//    @Autowired
//    private MessageMapper messageMapper;
//
//    public void saveMessage(Message message) {
//        message.setTimestamp(LocalDateTime.now());
//        message.setRead(false);
//        messageMapper.insert(message);
//    }
//}
