package com.giga.controller;

import com.giga.model.*;
import com.giga.service.CommentService;
import com.giga.service.LikeService;
import com.giga.service.QuestionService;
import com.giga.service.UserService;
import com.giga.utils.UtilBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	private QuestionService questionService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private HostHolder hostHolder;

	@RequestMapping(value = {"/question/{qid}"}, method = RequestMethod.GET)
	public String questionDetail(Model model, @PathVariable("qid") int qid) {
		Question question = questionService.getQuestion(qid);
		List<Comment> list = commentService.getCommentsByEntity(qid, EntityType.TYPE_COMMENT);
		ArrayList<ViewObject> vos = new ArrayList<>();
		for (Comment comment : list) {
			ViewObject vo = new ViewObject();
			vo.set("comment" , comment);

			if (hostHolder.getUser() == null) {
				vo.set("liked", 0);
			} else {
				vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(),
						EntityType.TYPE_COMMENT, comment.getId()));
			}
			vo.set("likeCount", likeService.getLikeCount(EntityType.TYPE_COMMENT, comment.getId()));
			vo.set("user", userService.getUser(comment.getUserId()));
			vos.add(vo);
		}
		model.addAttribute("questionId", qid);
		model.addAttribute("comments", vos);
		return "detail";
	}

	@RequestMapping(value = "/question/add", method = {RequestMethod.POST})
	@ResponseBody
	public String addQuestion(@RequestParam("title") String title,
							  @RequestParam("content") String content) {
		try {
			Question question = new Question();
			question.setTitle(title);
			question.setContent(content);
			question.setCreatedDate(new Date());
			question.setCommentCount(0);
			if (hostHolder.getUser() == null) {
				return UtilBox.getJSONString(999);
			} else {
				question.setUserId(hostHolder.getUser().getId());
			}
			if (questionService.addQuestion(question) > 0) {
				return UtilBox.getJSONString(0);
			}
		} catch (Exception e) {
			logger.error("增加题目失败" + e.getMessage());
		}
		return UtilBox.getJSONString(1, "失败");
	}

}
