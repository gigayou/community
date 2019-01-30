package com.giga.interceptor;

import com.giga.dao.LoginTicketDAO;
import com.giga.dao.UserDAO;
import com.giga.model.HostHolder;
import com.giga.model.LoginTicket;
import com.giga.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	@Autowired
	private HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		String ticket = null;
		if (httpServletRequest.getCookies() != null) {
			// 寻找token cookie
			for (Cookie cookie : httpServletRequest.getCookies()) {
				if (cookie.getName().equals("ticket")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}

		// 判断是否找到 token cookie
		if (ticket != null) {
			// 根据 ticket 字段查找对应的 loginTicket 对象
			LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
			// 判断 token 有效性
			if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
				return true;
			}
			User user = userDAO.selectById(loginTicket.getUserId());
			hostHolder.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null && hostHolder.getUser() != null) {
			modelAndView.addObject("user" , hostHolder.getUser());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		hostHolder.clear();
	}
}
