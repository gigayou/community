package com.giga.controller;

import com.giga.async.EventModel;
import com.giga.async.EventProducer;
import com.giga.async.EventType;
import com.giga.model.Comment;
import com.giga.model.EntityType;
import com.giga.model.HostHolder;
import com.giga.service.CommentService;
import com.giga.service.LikeService;
import com.giga.utils.UtilBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

	@Autowired
	private LikeService likeService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private CommentService commentService;

	@Autowired
	private EventProducer eventProducer;

	@RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
	@ResponseBody
	public String like(@RequestParam("commentId") int commentId) {
		if (hostHolder.getUser() == null) {
			return UtilBox.getJSONString(999);
		}

		Comment comment = commentService.getCommentById(commentId);
		// 点赞完成异步通知用户
		if (hostHolder.getUser().getId() != comment.getUserId()) {
			eventProducer.fireEvent(new EventModel(EventType.LIKE)
					.setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
					.setEntityType(EntityType.TYPE_COMMENT).setEntityOwnerId(comment.getUserId())
					.setExt("questionId", String.valueOf(comment.getEntityId())));
		}

		long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.TYPE_COMMENT, commentId);
		return UtilBox.getJSONString(0, String.valueOf(likeCount));
	}

	@RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
	@ResponseBody
	public String dislike(@RequestParam("commentId") int commentId) {
		if (hostHolder.getUser() == null) {
			return UtilBox.getJSONString(999);
		}

		long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.TYPE_COMMENT, commentId);
		return UtilBox.getJSONString(0, String.valueOf(likeCount));
	}
}
