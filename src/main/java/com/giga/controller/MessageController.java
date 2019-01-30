package com.giga.controller;

import com.giga.model.HostHolder;
import com.giga.model.Message;
import com.giga.model.User;
import com.giga.model.ViewObject;
import com.giga.service.MessageService;
import com.giga.service.UserService;
import com.giga.utils.UtilBox;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(Message.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private HostHolder hostHolder;

	@RequestMapping(path = {"/msg/jsonAddMessage"}, method = {RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
							 @RequestParam("toId") int toId,
							 @RequestParam("content") String content) {
		try {
			Message message = new Message();
			message.setFromId(fromId);
			message.setToId(toId);
			message.setContent(content);
			message.setHasRead(0);
			message.setCreatedDate(new Date());

			message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
			messageService.addMessage(message);
			return UtilBox.getJSONString(message.getId());
		} catch (Exception e) {
			logger.error("增加站内信失败" + e.getMessage());
			return UtilBox.getJSONString(1, "插入站内信失败");
		}
	}

	@RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName,
							 @RequestParam("content") String content) {
		try {
			// 判断当前用户是否是游客
			if (hostHolder.getUser() == null) {
				return UtilBox.getJSONString(999, "请先登陆");
			}
			User user = userService.selectByName(toName);
			if (user == null) {
				return UtilBox.getJSONString(999, "用户不存在");
			}

			Message message = new Message();
			message.setContent(content);
			message.setFromId(hostHolder.getUser().getId());
			message.setToId(user.getId());
			message.setHasRead(0);
			message.setCreatedDate(new Date());

			int fromId = message.getFromId();
			int toId = message.getToId();
			message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
			messageService.addMessage(message);
			return UtilBox.getJSONString(0);
		} catch (Exception e) {
			logger.error("发送消息失败" + e.getMessage());
			return UtilBox.getJSONString(1, "发信失败");
		}
	}

	@RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
	public String conversationDetail(Model model) {
		try {
			int localUserId = hostHolder.getUser().getId();
			List<ViewObject> conversations = new ArrayList<ViewObject>();
			List<Message> conversationList = messageService.getConversationList(localUserId, 0 ,10);
			for (Message msg : conversationList) {
				ViewObject vo = new ViewObject();
				vo.set("conversation", msg);
				int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
				User user = userService.getUser(targetId);
				vo.set("user", user);
				vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
				conversations.add(vo);
			}
			model.addAttribute("conversations", conversations);
		} catch (Exception e) {
			logger.error("获取站内信失败" + e.getMessage());
		}
		return "letter";
	}

	@RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
	public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
		try {
			// 更新message已读字段
			String[] ids = conversationId.split("_");
			int id = hostHolder.getUser().getId();
			for (String tid : ids) {
				if (!String.valueOf(id).equals(tid)) {
					messageService.updateConversationUnreadCount(Integer.valueOf(tid), conversationId);
				}
			}
			// 查找会话历史
			List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages = new ArrayList<>();
			for (Message msg : conversationList) {
				ViewObject vo = new ViewObject();
				vo.set("message", msg);
				User user = userService.getUser(msg.getFromId());
				if (user == null) {
					continue;
				}
				vo.set("headUrl", user.getHeadUrl());
				vo.set("userId", user.getId());
				messages.add(vo);
			}
			model.addAttribute("messages", messages);
		} catch (Exception e) {
			logger.error("获取详情消息失败" + e.getMessage());
		}
		return "letterDetail";
	}



}
