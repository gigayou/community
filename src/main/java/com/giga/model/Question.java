package com.giga.model;

import java.util.Date;

public class Question {
	private Integer id;
	private String title;
	private String content;
	private Integer userId;
	private Date createdDate;
	private Integer commentCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"id\":")
				.append(id);
		sb.append(",\"title\":\"")
				.append(title).append('\"');
		sb.append(",\"content\":\"")
				.append(content).append('\"');
		sb.append(",\"userId\":")
				.append(userId);
		sb.append(",\"createdDate\":\"")
				.append(createdDate).append('\"');
		sb.append(",\"commentCount\":")
				.append(commentCount);
		sb.append('}');
		return sb.toString();
	}
}
