package jp.co.wordbook.controllers;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * infomation.jsp へパラメータを送るユーティリティ
 */
public class Information {

    /**
     * infomation.jsp へパラメータを送る
     * @param request       サーブレットが受け取るリクエスト
     * @param response      サーブレットが受け取るレスポンス
     * @param heading       見出し文
     * @param paragraph     説明文
     * @param buttonName    ボタン名
     * @param url           ボタンを押したときの遷移先
     */
    static public void forward(
        HttpServletRequest request, HttpServletResponse response,
        String heading, String paragraph, String buttonName, String url
    ) {
        try {
            // リクエストへ設定
            request.setAttribute("heading", heading);
            request.setAttribute("paragraph", paragraph);
            request.setAttribute("buttonName", buttonName);
            request.setAttribute("url", url);

            // JSPへ送信
            String view = "/WEB-INF/views/information.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(view);
            dispatcher.forward(request, response);

        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 「正しくないデータが送信されました。」ページへ遷移
     * @param request   サーブレットが受け取るリクエスト
     * @param response  サーブレットが受け取るレスポンス
     */
    static public void forwardDataWasIncorrect(
        HttpServletRequest request, HttpServletResponse response
    ) {
        Information.forward(
            request, response,
            "正しくないデータが送信されました。",
            "",
            "トップページへ",
            "top"
        );
    }
}
