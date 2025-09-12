package jp.co.wordbook.models;

import java.io.*;
import javax.servlet.http.*;

/**
 * usersテーブルのレコードを扱うクラス
 */
public class UserBean implements Serializable
{
    private String id;
    private String password;

    // コンストラクタ
    public UserBean() {}
    public UserBean(String id, String password) {
        this.id   = id;
        this.password = password;
    }

    // ゲッター / セッター
    public void setId(String id)             { this.id = id; }
    public void setPassword(String password) { this.password = password; }
    public String getId()                    { return id; }
    public String getPassword()              { return password; }


    //-------------------------------------------------------------------------
    // ログイン関係
    //-------------------------------------------------------------------------

    static final String ATTRIBUTE_NAME = "userid";

    /**
     * ログイン済みか否か
     * @param request   サーブレットが受け取るリクエスト
     * @return          true ログイン済み, false 未ログイン
     */
    public static boolean checkLoggedIn(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        return (
            session != null && 
            session.getAttribute(ATTRIBUTE_NAME) != null
        );
    }

    /**
     * セッションを設定
     * @param request   サーブレットが受け取るリクエスト
     * @param userId    usersテーブルのidカラム
     */
    public static void setSession(HttpServletRequest request, String userId) {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600); // 単位：秒 → 1時間に設定
        session.setAttribute(ATTRIBUTE_NAME, userId);
    }


    /**
     * ユーザーIDを取得
     * @param request   サーブレットが受け取るリクエスト
     * @return          usersテーブルのidカラム
     */
    public static String getUserId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        return (String)session.getAttribute(ATTRIBUTE_NAME);
    }

}