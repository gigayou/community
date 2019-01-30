package com.giga.async;

import com.alibaba.fastjson.JSONObject;
import com.giga.utils.JedisAdapter;
import com.giga.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

	@Autowired
	private JedisAdapter jedisAdapter;

	public boolean fireEvent(EventModel eventModel) {
		try {
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisUtil.getEventQueueKey();
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
