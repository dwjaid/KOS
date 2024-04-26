package com.kos.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kos.backend.mapper.BotMapper;
import com.kos.backend.pojo.Bot;
import com.kos.backend.pojo.User;
import com.kos.backend.service.user.bot.GetListService;
import com.kos.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetLIstServiceImpl implements GetListService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public List<Bot> getList() {
        User user = UserUtil.getUser();
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        return botMapper.selectList(queryWrapper);
    }
}
