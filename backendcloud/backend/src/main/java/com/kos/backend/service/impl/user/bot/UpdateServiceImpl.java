package com.kos.backend.service.impl.user.bot;

import com.kos.backend.mapper.BotMapper;
import com.kos.backend.pojo.Bot;
import com.kos.backend.pojo.User;
import com.kos.backend.service.user.bot.UpdateService;
import com.kos.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        User user = UserUtil.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));

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

        Bot bot = botMapper.selectById(bot_id);

        if (bot == null) {
            map.put("error_message", "Bot不存在或已被删除");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "你没有权限删除该Bot");
            return map;
        }

        Bot new_bot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getWinRate(),
                bot.getCreateTime(),
                new Date()

        );

        botMapper.updateById(new_bot);

        map.put("error_message", "success");

        return map;
    }
}
