package com.giga.dao;

import com.giga.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("commentDAO")
public interface CommentDAO {
	final static String TABLE_NAME = " COMMENT ";
	final static String INSERT_FIELDS = " CONTENT,USER_ID,ENTITY_ID,ENTITY_TYPE,CREATED_DATE,STATUS ";
	final static String SELECT_FIELDS = " ID," + INSERT_FIELDS;

	@Insert({"INSERT INTO", TABLE_NAME, "(", INSERT_FIELDS,
			") VALUES (#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
	int addComment(Comment comment);

	@Update({"UPDATE", TABLE_NAME, " SET STATUS=#{status} WHERE ENTITY_ID=#{entityId} AND ENTITY_TYPE=#{entityType}"})
	int updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE ENTITY_ID=#{entityId} AND ENTITY_TYPE=#{entityType}"})
	List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

	@Select({"SELECT COUNT(ID) FROM", TABLE_NAME, "WHERE ENTITY_ID=#{entityId} AND ENTITY_TYPE=#{entityType}"})
	int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE ID=#{id}"})
	Comment getCommentById(int id);
}
