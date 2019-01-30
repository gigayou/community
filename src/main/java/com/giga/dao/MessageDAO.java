package com.giga.dao;

import com.giga.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("messageDAO")
public interface MessageDAO {
	final static String TABLE_NAME = " MESSAGE ";
	final static String INSERT_FIELDS = " FROM_ID,TO_ID,CONTENT,CREATED_DATE,HAS_READ,CONVERSATION_ID ";
	final static String SELECT_FIELDS = " ID," + INSERT_FIELDS;

	@Insert({"INSERT INTO", TABLE_NAME, "(", INSERT_FIELDS,
			") VALUES (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
	int addMessage(Message message);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE CONVERSATION_ID=#{conversationId} ",
			"ORDER BY CREATED_DATE DESC LIMIT #{offset}, #{limit}"})
	List<Message> getConversationDetail(@Param("conversationId") String conversationId,
										@Param("offset") int offset, @Param("limit") int limit);

	@Select({"SELECT COUNT(ID) FROM", TABLE_NAME, "WHERE HAS_READ=0 AND TO_ID=#{userId} AND CONVERSATION_ID=#{conversationId}"})
	int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

	@Select({"SELECT",INSERT_FIELDS,",COUNT(ID) AS ID FROM ( SELECT * FROM", TABLE_NAME,
			" WHERE FROM_ID=#{userId} OR TO_ID=#{userId} ORDER BY ID DESC) TT ", "" +
			" GROUP BY CONVERSATION_ID ORDER BY CREATED_DATE DESC LIMIT #{offset},#{limit}"})
	List<Message> getConversationList(@Param("userId") int userId,
									  @Param("offset") int offset, @Param("limit") int limit);

	@Update({"UPDATE", TABLE_NAME, "SET HAS_READ=1 WHERE FROM_ID=#{userId} AND CONVERSATION_ID=#{conversationId}"})
	int updateConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
