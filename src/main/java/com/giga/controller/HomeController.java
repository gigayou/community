package com.giga.controller;

import com.giga.model.Question;
import com.giga.model.ViewObject;
import com.giga.service.QuestionService;
import com.giga.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private QuestionService questionService;

	private List<ViewObject> getQuestions(int userId, int offset, int limit) {
		List<Question> latestQuestions = questionService.getLatestQuestions(userId, offset, limit);
		List<ViewObject> vos = new ArrayList<ViewObject>();

		for (Question question : latestQuestions) {
			ViewObject vo = new ViewObject();
			vo.set("question", question);
			vo.set("user", userService.getUser(question.getUserId()));
			vos.add(vo);
		}
		return vos;
	}

	@RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String index(Model model,
						@RequestParam(value = "pop", defaultValue = "0") int pop) {
		model.addAttribute("vos", getQuestions(0, 0, 10));
		return "index";
	}

	@RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String userIndex(Model model, @PathVariable("userId") int userId) {
		model.addAttribute("vos", getQuestions(userId, 0, 10));
		return "index";
	}
}
