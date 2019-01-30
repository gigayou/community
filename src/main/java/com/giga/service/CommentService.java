package com.giga.service;

import com.giga.dao.CommentDAO;
import com.giga.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
	private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

	@Autowired
	private CommentDAO commentDAO;

	public List<Comment> getCommentsByEntity(int entityId, int entityType) {
		return commentDAO.selectByEntity(entityId, entityType);
	}

	public int addComment(Comment comment) {
		return commentDAO.addComment(comment);
	}

	public boolean deleteComment(int entityId, int entityType, int status) {
		return commentDAO.updateStatus(entityId, entityType, status) > 0;
	}

	public int getCommentCount(int entityId, int entityType) {
		return commentDAO.getCommentCount(entityId, entityType);
	}

	public Comment getCommentById(int commentId) {
		return commentDAO.getCommentById(commentId);
	}

}
