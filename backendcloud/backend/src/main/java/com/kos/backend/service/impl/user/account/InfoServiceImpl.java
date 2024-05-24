package com.kos.backend.service.impl.user.account;

import com.kos.backend.pojo.User;
import com.kos.backend.service.user.account.InfoService;
import com.kos.backend.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getinfo() {

        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("rating", user.getRating().toString());
        map.put("rank", user.getTierName());

        return map;
     }
}
