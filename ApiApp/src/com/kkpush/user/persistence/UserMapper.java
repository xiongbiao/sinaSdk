package com.kkpush.user.persistence;

import java.util.List;
import java.util.Map;

import com.kkpush.account.domain.Developer;
import com.kkpush.user.domain.UserInfo;


public interface UserMapper {

	void insertUser(UserInfo userInfo);
	 
}
