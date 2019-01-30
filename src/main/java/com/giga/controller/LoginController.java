package com.giga.controller;

import com.giga.async.EventModel;
import com.giga.async.EventProducer;
import com.giga.async.EventType;
import com.giga.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private EventProducer eventProducer;

	@RequestMapping(path = "/loginUI", method = {RequestMethod.GET})
	public String loginUI(Model model, @RequestParam(value = "next",required = false) String next) {
		model.addAttribute("next" ,next);
		return "login";
	}

	@RequestMapping(path = "/login/", method = {RequestMethod.POST})
	public String login(Model model, @RequestParam("username") String username,
						@RequestParam("password") String password,
						@RequestParam(value = "next", required = false) String next,
						@RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
						HttpServletResponse response) {
		try {
			Map<String, Object> map = userService.login(username, password);
			// 判断登录是否有效
			if (map.containsKey("ticket")) {
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 7);
				}
				response.addCookie(cookie);
				if (StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			} else {
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
		} catch (Exception e) {
			logger.error("登陆异常" + e.getMessage());
			return "login";
		}
	}

	@RequestMapping(path = "/register/", method = {RequestMethod.POST})
	public String register(Model model, @RequestParam("username") String username,
						   @RequestParam("password") String password,
						   @RequestParam(value = "next", required = false) String next,
						   @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
						   HttpServletResponse response) {
		try {
			Map<String, Object> map = userService.register(username, password);
			// 判断注册操作是否有效
			if (map.containsKey("ticket")) {
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 7);
				}
				response.addCookie(cookie);
				if (StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			} else {
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
		} catch (Exception e) {
			logger.error("注册异常" + e.getMessage());
			return "login";
		}
	}

	@RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
	public String logout(@CookieValue("ticket") String ticket) {
		userService.logout(ticket);

		return "redirect:/";
	}

}
