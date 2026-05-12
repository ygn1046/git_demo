package com.ygn.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygn.springboot.domain.UserImage;
import com.ygn.springboot.service.UserImageService;
import com.ygn.springboot.mapper.UserImageMapper;
import org.springframework.stereotype.Service;

/**
* @author 10467
* @description 针对表【user_image】的数据库操作Service实现
* @createDate 2026-03-04 01:17:25
*/
@Service
public class UserImageServiceImpl extends ServiceImpl<UserImageMapper, UserImage>
    implements UserImageService{

}




