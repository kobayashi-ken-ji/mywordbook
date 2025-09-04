<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.Quiz" %>
<%
    // 科目名
    String subjectName = (String)request.getAttribute("subjectname");

    // 問題セット
    List<Quiz> quizzes = (List<Quiz>)request.getAttribute("quizzes");
    int quizListSize = quizzes.size();
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">
            
            <div class="white-area">

                <h1><%= subjectName %></h1>
                <p>問題数 : <%= quizListSize %></p>

                <a class="button">問題を追加</a>
                <br><br><br>

                <div class="search-box">
                    <input type="text" placeholder="ID、単語など">
                    <button>検索</button>
                </div>

                <table class="td-align-left">
                    <tr>
                        <th>ID</th><th>問題文</th>
                    </tr>
                    <% for (Quiz quiz : quizzes) { 
                        int    id       = quiz.id;
                        String question = quiz.question;
                        String link     = "quizdetails?quizid=" + id;
                    %>
                        <tr>
                            <th><%= id %></td>
                            <td><a href="<%= link %>"><%= question %></a></td>
                        </tr>
                    <% } %>
                </table>

            </div>

            <a class="button red wide" onclick="history.back()">戻る</a>
        </div>
    </body>
</html>