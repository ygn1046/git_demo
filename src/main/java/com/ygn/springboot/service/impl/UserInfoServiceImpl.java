package com.ygn.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygn.springboot.domain.UserInfo;
import com.ygn.springboot.service.UserInfoService;
import com.ygn.springboot.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 10467
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2026-03-04 01:16:55
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




