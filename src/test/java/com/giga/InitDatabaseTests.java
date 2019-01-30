package com.giga;

import com.giga.dao.QuestionDAO;
import com.giga.dao.UserDAO;
import com.giga.model.Question;
import com.giga.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommunityApplication.class)
public class InitDatabaseTests {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private QuestionDAO questionDAO;

	@Test
	public void contextLoads() {
//		Random random = new Random();
		for (int i = 0; i < 11; i++){
//			User user = new User();
//			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//			user.setName(String.format("USER%d", i));
//			user.setPassword("");
//			user.setSalt("");
//			userDAO.addUser(user);
			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
			question.setCreatedDate(date);
			question.setUserId(i + 1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("Balaababalalalal Content %d", i));
			questionDAO.addQuestion(question);
		}


//		User user = new User();
//		user.setId(1);
//		user.setPassword("test");
//		userDAO.updatePassword(user);
//		Assert.assertEquals("test", userDAO.selectById(1).getPassword());
	}
}
