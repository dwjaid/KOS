package com.kos.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kos.backend.mapper.BotMapper;
import com.kos.backend.pojo.Bot;
import com.kos.backend.pojo.User;
import com.kos.backend.service.user.bot.AddService;
import com.kos.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {

        User user = UserUtil.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if (title == null || title.isEmpty()) {
            map.put("error_message", "标题不能为空");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }

        if (description == null || description.isEmpty()) {
            description = "这个用户很懒，什么也没有写";
        }

        if (description.length() > 500) {
            map.put("error_message", "描述长度不能大于500");
            return map;
        }

        if (content == null || content.isEmpty()) {
            map.put("error_message", "代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "代码长度不能超过10000");
            return map;
        }
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(queryWrapper) >= 10) {
            map.put("error_message", "每个用户最多只能创建10个Bot!");
            return map;
        }
        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, 0.0, now, now);
        botMapper.insert(bot);
        map.put("error_message", "success");
        return map;
    }
}
