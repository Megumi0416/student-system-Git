package com.example.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.example.service.impl.SpringContextUtil;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 服务
 */
@ServerEndpoint(value = "/imserver/{username}")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 手动从 Spring 上下文中获取 UserService
     */
    private UserService userService;

    public WebSocketServer() {
        this.userService = SpringContextUtil.getBean(UserService.class);
    }

    /**
     * 在线用户的 Session 映射：username -> Session
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 用户名和姓名的映射：username -> name
     */
    public static final Map<String, String> usernameNameMap = new ConcurrentHashMap<>();

    // 头像映射
    public static final Map<String, String> usernameAvatarMap = new ConcurrentHashMap<>();
    // 用户角色
    public static final Map<String, String> usernameRoleMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        // 将用户加入 sessionMap
        sessionMap.put(username, session);

        // 通过 UserService 获取姓名
        String name = userService.getNameByUsername(username);
        usernameNameMap.put(username, name);

        String avatar = userService.getAvatarByUsername(username);
        usernameAvatarMap.put(username, avatar);

        String role = userService.getRoleByUsername(username);
        usernameRoleMap.put(username, role);

        log.info("有新用户加入，username={}, name={}, 当前在线人数为：{}", username, name, sessionMap.size());

        // 广播更新用户列表
        broadcastUserList();

//        // 广播更新用户列表
//        JSONObject result = new JSONObject();
//        JSONArray array = new JSONArray();
//        result.set("users", array);
//        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
//            String currentUsername = entry.getKey();
//            String currentName = usernameNameMap.get(currentUsername);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.set("username", currentUsername);
//            jsonObject.set("name", currentName);
//            array.add(jsonObject);
//        }
//        sendAllMessage(JSONUtil.toJsonStr(result));
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        // 从映射中移除用户
        sessionMap.remove(username);
        usernameNameMap.remove(username);

        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", username, sessionMap.size());

        // 广播更新用户列表
        broadcastUserList();

//        // 广播更新用户列表
//        JSONObject result = new JSONObject();
//        JSONArray array = new JSONArray();
//        result.set("users", array);
//        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
//            String currentUsername = entry.getKey();
//            String currentName = usernameNameMap.get(currentUsername);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.set("username", currentUsername);
//            jsonObject.set("name", currentName);
//            array.add(jsonObject);
//        }
//        sendAllMessage(JSONUtil.toJsonStr(result));
    }

    /**
     * 统一的用户列表广播方法
     */
    private void broadcastUserList() {
        JSONObject result = new JSONObject();
        JSONArray userArray = new JSONArray();

        sessionMap.forEach((username, session) -> {
            JSONObject userObj = new JSONObject();
            userObj.set("username", username)
                    .set("name", usernameNameMap.get(username))
                    .set("avatar", usernameAvatarMap.get(username));
            userArray.add(userObj);
        });

        result.set("users", userArray);
        sendAllMessage(JSONUtil.toJsonStr(result));
    }

    /**
     * 收到客户端消息
     */
//    @OnMessage
//    public void onMessage(String message, Session session, @PathParam("username") String username) {
//        log.info("服务端收到用户username={}的消息: {}", username, message);
//
//        JSONObject obj = JSONUtil.parseObj(message);
//        String toUsername = obj.getStr("to"); // 接收人用户名
//        String text = obj.getStr("text"); // 消息内容
//
//        Session toSession = sessionMap.get(toUsername);
//        String fromName = usernameNameMap.get(username); // 发送人姓名
//
//        if (toSession != null) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.set("from", fromName);
//            jsonObject.set("text", text);
//            this.sendMessage(jsonObject.toString(), toSession);
//            log.info("发送给用户username={}, 消息：{}", toUsername, jsonObject.toString());
//        } else {
//            log.info("发送失败，未找到用户username={}的session", toUsername);
//        }
//    }
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("服务端收到用户username={}的消息: {}", username, message);
        JSONObject obj = JSONUtil.parseObj(message);
        String toUsername = obj.getStr("to");
        String text = obj.getStr("text");

        Session toSession = sessionMap.get(toUsername);
        String fromName = usernameNameMap.get(username); // 发送人姓名

        if (toSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", username);         // 使用用户名作为标识
            jsonObject.set("fromName", fromName);     // 携带姓名用于展示
            jsonObject.set("fromAvatar", usernameAvatarMap.get(username));
            jsonObject.set("text", text);
            this.sendMessage(jsonObject.toString(), toSession);
            log.info("发送给用户username={}, 消息：{}", toUsername, jsonObject.toString());
        } else {
            log.info("发送失败，未找到用户username={}的session", toUsername);
        }

        // 保存消息到数据库
        Message dbMessage = new Message();
        dbMessage.setSenderUsername(username);
        dbMessage.setSenderName(usernameNameMap.get(username));
        dbMessage.setSenderRole(usernameRoleMap.get(username));
        dbMessage.setReceiverUsername(toUsername);
        dbMessage.setReceiverName(userService.getNameByUsername(toUsername));
        dbMessage.setReceiverRole(userService.getRoleByUsername(toUsername));
        dbMessage.setContent(text);
        dbMessage.setTimestamp(new Date());
//        dbMessage.setIsRead(false);
        dbMessage.setIsRead(toSession != null);

        MessageMapper messageMapper = SpringContextUtil.getBean(MessageMapper.class);
        messageMapper.insert(dbMessage);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息: {}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端广播消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息: {}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}