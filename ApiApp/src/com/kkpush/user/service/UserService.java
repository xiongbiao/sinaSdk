package com.kkpush.user.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.user.domain.UserInfo;
import com.kkpush.user.persistence.UserMapper;

@Service
@Transactional
public class UserService {
	@Autowired
	UserMapper userMapper;

	private static Logger logger = LoggerFactory
	.getLogger(UserService.class);

	public void insertUser(UserInfo user) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("email", user.getEmail());

//		if (isExistsEmail(param)) {
//			throw new Exception("邮件已经被注册");
//
//		}
//		param = new HashMap<String, Object>();
//		param.put("devName", developer.getDevName());
//		if (isExistsName(param)) {
//			throw new Exception("用户名已经被注册");
//		}
		// 新增开发者
		userMapper.insertUser(user);
	}
 
}
