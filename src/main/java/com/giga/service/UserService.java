package com.giga.service;

import com.giga.dao.LoginTicketDAO;
import com.giga.dao.UserDAO;
import com.giga.model.LoginTicket;
import com.giga.model.User;
import com.giga.utils.UtilBox;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private LoginTicketDAO loginTicketDAO;

	public User getUser(int id) {
		return userDAO.selectById(id);
	}

	public User selectByName(String name) {
		return userDAO.selectByName(name);
	}

	public Map<String, Object> register(String name, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(name)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		User user = userDAO.selectByName(name);
		if (user != null) {
			map.put("msg", "该用户名已被注册");
			return map;
		}

		user = new User();
		user.setName(name);
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
		user.setHeadUrl(head);
		user.setPassword(UtilBox.MD5(password + user.getSalt()));
		userDAO.addUser(user);

		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket);
		return map;
	}

	public Map<String, Object> login(String name, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 判断用户名是否为空
		if (StringUtils.isBlank(name)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		// 判断密码是否为空
		if (StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		// 根据用户名查询 user model
		User user = userDAO.selectByName(name);
		if (user == null) {
			map.put("msg", "用户名不存在");
			return map;
		}
		// 根据页面上输入的密码组合用户的salt字段并MD5化，其结果与原密码匹配
		String comboPwd = UtilBox.MD5(password + user.getSalt());
		if (!comboPwd.equals(user.getPassword())) {
			map.put("msg", "密码错误");
			return map;
		}
		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket);
		return map;
	}

	/**
	 * 添加token
	 *
	 * @param userId 用户id
	 * @return ticket
	 */
	public String addLoginTicket(Integer userId) {
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		Date date = new Date();
		date.setTime(date.getTime() + 3600*24*10);
		ticket.setExpired(date);
		ticket.setStatus(0);
		loginTicketDAO.addTicket(ticket);
		return ticket.getTicket();
	}

	public void logout(String ticket) {
		loginTicketDAO.updateStatus(ticket, 1);
	}

}
