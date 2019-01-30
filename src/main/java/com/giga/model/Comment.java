package com.giga.model;

import java.util.Date;

public class Comment {
	private Integer id;
	private String content;
	private Integer userId;
	private Integer entityId;
	private Integer entityType;
	private Date createdDate;
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"id\":")
				.append(id);
		sb.append(",\"content\":\"")
				.append(content).append('\"');
		sb.append(",\"userId\":")
				.append(userId);
		sb.append(",\"entityId\":")
				.append(entityId);
		sb.append(",\"entityType\":")
				.append(entityType);
		sb.append(",\"createdDate\":\"")
				.append(createdDate).append('\"');
		sb.append(",\"status\":")
				.append(status);
		sb.append('}');
		return sb.toString();
	}
}
