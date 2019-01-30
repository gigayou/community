package com.giga.service;

import com.giga.dao.MessageDAO;
import com.giga.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	@Autowired
	MessageDAO messageDAO;

	@Autowired
	SensitiveService sensitiveService;

	public int addMessage(Message message) {
		// 防止xss脚本注入供给 & 过滤敏感词
		message.setContent(HtmlUtils.htmlEscape(message.getContent()));
		message.setContent(sensitiveService.filter(message.getContent()));
		return messageDAO.addMessage(message);
	}

	public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
		return messageDAO.getConversationDetail(conversationId, offset, limit);
	}

	public List<Message> getConversationList(int userId, int offset, int limit) {
		return messageDAO.getConversationList(userId, offset, limit);
	}

	public int getConversationUnreadCount(int userId, String conversationId) {
		return messageDAO.getConversationUnreadCount(userId, conversationId);
	}

	public int updateConversationUnreadCount(int userId, String conversationId) {
		return messageDAO.updateConversationUnreadCount(userId, conversationId);
	}

}
