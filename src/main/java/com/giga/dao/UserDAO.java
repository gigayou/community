package com.giga.dao;

import com.giga.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Component("userDAO")
public interface UserDAO {
	final static String TABLE_NAME = " USER ";
	final static String INSERT_FIELDS = " NAME,PASSWORD,SALT,HEAD_URL ";
	final static String SELECT_FIELDS = " ID," + INSERT_FIELDS;

	@Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS,
			") VALUES (#{name},#{password},#{salt},#{headUrl})"})
	int addUser(User user);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME,
			"WHERE ID=#{id}"})
	User selectById(int id);

	@Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "" +
			"WHERE NAME=#{name}"})
	User selectByName(@Param("name") String name);

	@Update({"UPDATE", TABLE_NAME, "SET PASSWORD=#{password} WHERE ID=#{id}"})
	void updatePassword(User user);

	@Delete({"DELETE FROM", TABLE_NAME, "WHERE ID=#{id}"})
	void deleteById(int id);

}
