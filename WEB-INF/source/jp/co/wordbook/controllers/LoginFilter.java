package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

// 未ログインの場合、ログインページへ遷移させるフィルタ
@WebFilter
public class LoginFilter implements Filter {
    
    public void doFilter
        (ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        // 型変換
        HttpServletRequest request2 = (HttpServletRequest)request;
        HttpServletResponse response2 = (HttpServletResponse)response;

        // ログイン済みか否か
        HttpSession session = request2.getSession(false);
        boolean loggedIn = 
            (session != null && session.getAttribute("userid") != null); 

        // リクエストがログイン画面か否か
        // (ログイン画面はセッションが無くても遷移可)
        String loginURI = request2.getContextPath() + "/login";
        boolean loginRequest =
            request2.getRequestURI().equals(loginURI) ||
            request2.getRequestURI().endsWith("/login") ||
            request2.getRequestURI().endsWith("style.css");

        // ログイン済 → 次のフィルター、またはサーブレットへ
        if (loggedIn || loginRequest)
            chain.doFilter(request, response);
        
        // 未ログイン → ログイン画面へ
        else response2.sendRedirect(loginURI);
    }

    public void init(FilterConfig config) throws ServletException {}
    public void destroy() {}
}
