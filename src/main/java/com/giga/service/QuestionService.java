package com.giga.service;

import com.giga.dao.QuestionDAO;
import com.giga.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
	private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

	@Autowired
	private QuestionDAO questionDAO;

	@Autowired
	private SensitiveService sensitiveService;

	public List<Question> getLatestQuestions(int userId, int offset, int limit) {
		return questionDAO.selectLatestQuestions(userId, offset, limit);
	}

	public int addQuestion(Question question) {
		// 防止xss脚本注入攻击
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		// 敏感词过滤
		question.setTitle(sensitiveService.filter(question.getTitle()));
		question.setContent(sensitiveService.filter(question.getContent()));
		return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
	}

	public Question getQuestion(int id) {
		return questionDAO.getById(id);
	}

	public int updateCommentCount(int id, int commentCount) {
		return questionDAO.updateCommentCount(id, commentCount) > 0 ? id : 0;
	}

	public int getUserIdByQid(int questionId) {
		return questionDAO.getUserIdByQid(questionId);
	}
}
