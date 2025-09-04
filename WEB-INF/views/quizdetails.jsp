<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.Quiz" %>
<%
    // 問題
    Quiz quiz = (Quiz)request.getAttribute("quiz");
    int    id              = (Integer)quiz.id;              // クイズID
    int    subject_id      = (Integer)quiz.subject_id;      // 科目ID
    int    difficulty_id   = (Integer)quiz.difficulty_id;   // 難易度
    String explanation     = (String)quiz.explanation;      // 説明文
    String question        = (String)quiz.question;         // 問題文
    String answer          = (String)quiz.answer;           // 正解文

    String subject_name    = (String)request.getAttribute("subject_name");    // 科目名
    String difficulty_name = (String)request.getAttribute("difficulty_name"); // 難易度名
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">
            <div class="white-area">

                <h1>問題詳細</h1>

                <!-- テーブルヘッダーを縦並べ -->
                <table class="td-align-left ">
                    <tr>
                        <th>ID</th>
                        <td><%= id %></td>
                    </tr>
                    <tr>
                        <th>科目</th>
                        <td><%= subject_name %></td>
                    </tr>
                    <tr>
                        <th>問題</th>
                        <td><%= question %></td>
                    </tr>
                    <tr>
                        <th>正解</th>
                        <td><%= answer %></td>
                    </tr>
                    <tr>
                        <th>説明</th>
                        <td>
                            <%= explanation %><br>
                        </td>
                    </tr>
                    <tr>
                        <th>難易度</th>
                        <td><%= difficulty_name %></td>
                    </tr>
                </table>
                <br>
                
                <a class="button">編集</a>
                <a class="button">削除</a>
            </div>

            <a class="button red wide" onclick="history.back()">
                戻る
            </a>
        </div>
    </body>
</html>