package com.giga.model;

public class User {

	private Integer id;
	private String name;
	private String password;
	private String salt;
	private String headUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"id\":")
				.append(id);
		sb.append(",\"name\":\"")
				.append(name).append('\"');
		sb.append(",\"password\":\"")
				.append(password).append('\"');
		sb.append(",\"slat\":\"")
				.append(salt).append('\"');
		sb.append(",\"headUrl\":\"")
				.append(headUrl).append('\"');
		sb.append('}');
		return sb.toString();
	}
}
