package com.giga.dao;

import com.giga.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("questionDAO")
public interface QuestionDAO {
	final static String TABLE_NAME = " QUESTION ";
	final static String INSERT_FIELDS = " TITLE,CONTENT,USER_ID,CREATED_DATE,COMMENT_COUNT ";
	final static String SELECT_FIELDS = " ID," + INSERT_FIELDS;

	@Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS,
			") VALUES (#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
	int addQuestion(Question question);

	List<Question> selectLatestQuestions(@Param("userId") int userId,
										 @Param("offset") int offset,
										 @Param("limit") int limit);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE ID=#{id}"})
	Question getById(int id);

	@Update({"UPDATE", TABLE_NAME,"SET COMMENT_COUNT=#{commentCount} WHERE ID = #{id}"})
	int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

	@Select({"SELECT USER_ID FROM", TABLE_NAME, "WHERE ID=#{id}"})
	int getUserIdByQid(int id);

}
