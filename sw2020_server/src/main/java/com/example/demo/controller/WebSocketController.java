package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Controller;

import com.example.demo.exception.MyException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ServerEndpoint("/wsserver/{type}/{userId}/{courseId}")

public class WebSocketController {

	private final static ConcurrentHashMap<String, WebSocketController> teacherWebSocketMap = new ConcurrentHashMap<>();

	private final static ConcurrentHashMap<String, WebSocketController> studentWebSocketMap = new ConcurrentHashMap<>();

	private final static ConcurrentHashMap<String, List<String>> studentMap = new ConcurrentHashMap<>();
	
	private final static ConcurrentHashMap<String, String> courseMap = new ConcurrentHashMap<>();

	private String userId;

	private Session session;
	

	@OnOpen
	public void onOpen(Session session, @PathParam("type") String type, @PathParam("userId") String userId,
			@PathParam("courseId") String courseId) throws MyException {
		this.userId = userId;
		this.session = session;
		// 教师使用
		if (type.equals("teacher")) {
			if (teacherWebSocketMap.containsKey(userId)) {
				teacherWebSocketMap.remove(userId);
				teacherWebSocketMap.put(userId, this);
			} else {
				teacherWebSocketMap.put(userId, this);
				studentMap.put(userId, Collections.synchronizedList(new ArrayList<String>()));
				courseMap.put(courseId, userId);
			}
		}
		// 学生使用
		else if (type.equals("student")) {
			String teacherId = courseMap.get(courseId);
			if (teacherId == null) {
				throw new MyException("老师未开课");
			} else {
				if (teacherWebSocketMap.containsKey(teacherId)) {
					List<String> students = studentMap.get(teacherId);
					students.add(userId);
					studentWebSocketMap.put(userId, this);
				} else {
					throw new MyException("老师未开课");
				}
			}
		}

		log.info("socket用户:" + userId);
	}

	@OnClose
	public void onClose() {
		if (teacherWebSocketMap.containsKey(userId)) {
			teacherWebSocketMap.remove(userId);
		}
		log.info("用户退出:" + userId);
	}

	/**
	 *
	 * @param session
	 * @param error
	 * @throws MyException 
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		log.info(userId + "用户报文" + message);
		List<String> students = studentMap.get(userId);
		for (String student : students) {
			studentWebSocketMap.get(student).session.getBasicRemote().sendText(message);
		}
	}
}
