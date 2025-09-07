package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogoutServlet extends HttpServlet {

    // ログアウト処理
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // セッションが存在するなら無効化
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        
        // ログイン画面へリダイレクト
        response.sendRedirect("login");
    }
}
