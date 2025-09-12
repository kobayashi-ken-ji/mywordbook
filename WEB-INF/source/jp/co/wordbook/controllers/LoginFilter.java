package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import jp.co.wordbook.models.Session;

// 未ログインの場合、ログインページへ遷移させるフィルタ
@WebFilter(urlPatterns="/*")
public class LoginFilter implements Filter {
    
    public void doFilter
        (ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        // 型変換
        HttpServletRequest hRequest = (HttpServletRequest)request;
        HttpServletResponse hResponse = (HttpServletResponse)response;

        // ログイン済みか否か
        boolean loggedIn = Session.checkLoggedIn(hRequest);

        // リクエストがログイン画面か否か
        // (ログイン画面はセッションが無くても遷移可)
        String uri = hRequest.getRequestURI();
        boolean loginRequest =
            uri.endsWith("/login") ||
            uri.endsWith("style.css");

        // ログイン済 → 次のフィルター、またはサーブレットへ
        if (loggedIn || loginRequest)
            chain.doFilter(request, response);
        
        // 未ログイン → ログイン画面へ
        else hResponse.sendRedirect("./login");
    }

    public void init(FilterConfig config) throws ServletException {}
    public void destroy() {}
}
