package com.kos.backend.consumer.utils;

import com.kos.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthentication {
    public static Integer getUsetId(String token) {
        int userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject() );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
