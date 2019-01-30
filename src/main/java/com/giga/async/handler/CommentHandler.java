package com.giga.async.handler;

import com.giga.async.EventHandler;
import com.giga.async.EventModel;
import com.giga.async.EventType;
import com.giga.model.Message;
import com.giga.model.User;
import com.giga.service.CommentService;
import com.giga.service.MessageService;
import com.giga.service.UserService;
import com.giga.utils.UtilBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CommentHandler implements EventHandler {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Override
	public void doHandler(EventModel model) {
		Message message = new Message();
		message.setFromId(UtilBox.SYSTEM_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		message.setConversationId(String.format("%d_%d",
				message.getFromId(), message.getToId()));
		message.setHasRead(0);
		User user = userService.getUser(model.getActorId());
		message.setContent("用户" + user.getName()
				+ "评论了你的问题：http://127.0.0.1:8080/question/"
				+ model.getExt("questionId"));
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.COMMENT);
	}
}
