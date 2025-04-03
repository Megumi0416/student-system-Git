package com.example.controller;

import com.example.common.Result;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageMapper messageMapper;
    @Resource // 新增注入UserService
    private UserService userService;

    @GetMapping("/list")
    public Result getMessageHistory(
            @RequestParam String from,
            @RequestParam String to) {
        List<Message> messages = messageMapper.selectConversation(from, to);
        List<Map<String, Object>> result = messages.stream().map(msg -> {
            Map<String, Object> map = new HashMap<>();
            map.put("senderUsername", msg.getSenderUsername());
            map.put("senderName", userService.getNameByUsername(msg.getSenderUsername()));
            map.put("senderAvatar", userService.getAvatarByUsername(msg.getSenderUsername()));
            map.put("receiverUsername", msg.getReceiverUsername());
            map.put("receiverName", userService.getNameByUsername(msg.getReceiverUsername()));
            map.put("receiverAvatar", userService.getAvatarByUsername(msg.getReceiverUsername()));
            map.put("text", msg.getContent());
            map.put("timestamp", msg.getTimestamp().getTime());
            return map;
        }).collect(Collectors.toList());
        return Result.success(result);
    }
}
