package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import jp.co.wordbook.models.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // ログイン画面へ
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setAttribute("message", "");

        String view = "/WEB-INF/views/login.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }


    // ログイン処理
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // リクエストから取得
        request.setCharacterEncoding("utf-8");
        String userId = request.getParameter("userid");
        String password = request.getParameter("password");

        // 認証処理
        boolean isAuthenticated = new UserDAO().searchUser(userId, password);

        // 認証成功 → セッションを設定 / トップ画面へ
        if (isAuthenticated) {
            Session.setSession(request, userId);
            response.sendRedirect("top");
        }
        
        // 認証失敗 → 再度ログイン画面へ
        else {
            String message = "ユーザーIDまたはパスワードが間違っています。";
            request.setAttribute("message", message);

            String view = "/WEB-INF/views/login.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(view);
            dispatcher.forward(request, response);
        }
    }
}
