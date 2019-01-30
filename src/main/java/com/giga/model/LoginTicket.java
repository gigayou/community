package com.giga.model;

import java.util.Date;

public class LoginTicket {

	private Integer id;
	private Integer userId;
	private String ticket;
	private Date expired;
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
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
		sb.append(",\"userId\":")
				.append(userId);
		sb.append(",\"ticket\":\"")
				.append(ticket).append('\"');
		sb.append(",\"expired\":\"")
				.append(expired).append('\"');
		sb.append(",\"status\":")
				.append(status);
		sb.append('}');
		return sb.toString();
	}
}
