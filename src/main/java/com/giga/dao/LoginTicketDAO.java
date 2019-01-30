package com.giga.dao;

import com.giga.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component("loginTicketDAO")
public interface LoginTicketDAO {
	final static String TABLE_NAME = " LOGIN_TICKET ";
	final static String INSERT_FIELDS = " USER_ID,TICKET,EXPIRED,STATUS ";
	final static String SELECT_FIELDS = " ID," + INSERT_FIELDS;

	@Insert({"INSERT INTO", TABLE_NAME, "(", INSERT_FIELDS,
			") VALUES (#{userId},#{ticket},#{expired},#{status})"})
	int addTicket(LoginTicket ticket);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME,
			"WHERE TICKET=#{ticket}"})
	LoginTicket selectByTicket(@Param("ticket") String ticket);

	@Update({"UPDATE", TABLE_NAME,"SET STATUS=#{status} " +
			"WHERE TICKET=#{ticket}"})
	void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
