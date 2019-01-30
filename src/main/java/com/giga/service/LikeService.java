package com.giga.service;

import com.giga.utils.JedisAdapter;
import com.giga.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

	@Autowired
	JedisAdapter jedisAdapter;

	public long getLikeCount(int entityType, int entityId) {
		String likeKey = RedisUtil.getLikeKey(entityType, entityId);
		return jedisAdapter.scard(likeKey);
	}

	/**
	 * 获取当前登录用户态度:
	 * 		-1 - 踩
	 * 		0  - 无
	 * 		1  - 赞
	 *
	 * @param userId 用户id
	 * @param entityType 事件类型 (question/comment)
	 * @param entityId	事件id
	 * @return
	 */
	public int getLikeStatus(int userId, int entityType, int entityId) {
		String likeKey = RedisUtil.getLikeKey(entityType, entityId);
		if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
			return 1;
		}

		String dislikeKey = RedisUtil.getDislikeKey(entityType, entityId);
		if (jedisAdapter.sismember(dislikeKey, String.valueOf(userId))) {
			return -1;
		}

		return 0;
	}

	public long like(int userId, int entityType, int entityId) {
		String likeKey = RedisUtil.getLikeKey(entityType, entityId);
		jedisAdapter.sadd(likeKey, String.valueOf(userId));

		String dislikeKey = RedisUtil.getDislikeKey(entityType, entityId);
		jedisAdapter.srem(dislikeKey, String.valueOf(userId));

		return jedisAdapter.scard(likeKey);
	}

	public long dislike(int userId, int entityType, int entityId) {
		String dislikeKey = RedisUtil.getDislikeKey(entityType, entityId);
		jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

		String likeKey = RedisUtil.getLikeKey(entityType, entityId);
		jedisAdapter.srem(likeKey, String.valueOf(userId));

		return jedisAdapter.scard(likeKey);
	}

}
