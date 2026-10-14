package com.alunos.scholl.infra.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class SearchForCookie {
    public static String obterCookieHttpOnly(HttpServletRequest request, String nomeCookie) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(nomeCookie)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
