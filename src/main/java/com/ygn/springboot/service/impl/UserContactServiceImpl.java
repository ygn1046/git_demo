package com.ygn.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygn.springboot.domain.UserContact;
import com.ygn.springboot.service.UserContactService;
import com.ygn.springboot.mapper.UserContactMapper;
import org.springframework.stereotype.Service;

/**
* @author 10467
* @description 针对表【user_contact】的数据库操作Service实现
* @createDate 2026-03-04 01:17:32
*/
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact>
    implements UserContactService{

}




