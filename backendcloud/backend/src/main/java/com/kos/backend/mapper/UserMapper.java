package com.kos.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kos.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {


}
