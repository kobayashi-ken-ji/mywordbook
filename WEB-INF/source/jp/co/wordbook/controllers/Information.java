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
     * @param buttonname    ボタン名
     * @param url           ボタンを押したときの遷移先
     */
    static public void forward(
        HttpServletRequest request, HttpServletResponse response,
        String heading, String paragraph, String buttonname, String url
    ) {
        try {
            // リクエストへ設定
            request.setAttribute("heading", heading);
            request.setAttribute("paragraph", paragraph);
            request.setAttribute("buttonname", buttonname);
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
     * 「送信されたデータが正しくありませんでした。」ページへ遷移
     * @param request   サーブレットが受け取るリクエスト
     * @param response  サーブレットが受け取るレスポンス
     */
    static public void forwardDataWasIncorrect(
        HttpServletRequest request, HttpServletResponse response
    ) {
        Information.forward(
            request, response,
            "送信されたデータが正しくありませんでした。",
            "",
            "トップページへ",
            "top"
        );
    }
}

/*
        // パラメータから取得
        int quiz_id;
        int subject_id;
        try {
            quiz_id    = Integer.parseInt(request.getParameter("id"));
            subject_id = Integer.parseInt(request.getParameter("subjectid"));
        }

        // 不正な入力 → インフォメーションページへ
        catch (NumberFormatException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }


        // 科目とユーザーが紐づいていない → 不正入力のインフォメーションページへ
        if (! subjectDAO.userHasSubject(subject_id, userId)) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }
            
        // セッションから取得
        String userId = Session.getUserId(request);
 */