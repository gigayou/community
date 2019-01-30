package com.giga.controller;

import com.giga.async.EventModel;
import com.giga.async.EventProducer;
import com.giga.async.EventType;
import com.giga.model.Comment;
import com.giga.model.EntityType;
import com.giga.model.HostHolder;
import com.giga.service.CommentService;
import com.giga.service.QuestionService;
import com.giga.service.SensitiveService;
import com.giga.service.UserService;
import com.giga.utils.UtilBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
public class CommentController {
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private SensitiveService sensitiveService;

	@Autowired
	private EventProducer eventProducer;

	@RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
	public String addComment(@RequestParam("questionId") int questionId,
							 @RequestParam("content") String content) {
		try {
			// 防止XSS脚本注入攻击 & 去敏感词
			content = HtmlUtils.htmlEscape(content);
			content = sensitiveService.filter(content);

			Comment comment = new Comment();
			if (hostHolder.getUser() == null) {
				comment.setUserId(UtilBox.ANONYMOUS_USER_ID);
			} else {
				comment.setUserId(hostHolder.getUser().getId());
			}
			comment.setContent(content);
			comment.setEntityId(questionId);
			comment.setEntityType(EntityType.TYPE_COMMENT);
			comment.setCreatedDate(new Date());
			comment.setStatus(0);

			int commentId = commentService.addComment(comment);
			// 获取提出当前问题的用户的id
			int qUserId = questionService.getUserIdByQid(questionId);
			// 他人完成评论，异步通知提出该问题的用户
			if (hostHolder.getUser().getId() != qUserId){
				eventProducer.fireEvent(new EventModel(EventType.COMMENT)
						.setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
						.setEntityType(EntityType.TYPE_COMMENT).setEntityOwnerId(qUserId)
						.setExt("questionId", String.valueOf(questionId)));
			}

			// 更新评论数
			int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
			questionService.updateCommentCount(comment.getEntityId(), count);
		} catch (Exception e) {
			logger.error("增加评论失败" + e.getMessage());
		}
		return "redirect:/question/" + String.valueOf(questionId);
	}

}
