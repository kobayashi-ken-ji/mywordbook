<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.Subject" %>
<%
    // 科目リスト
    List<Subject> subjects = (List<Subject>)request.getAttribute("subjects");

    // 問題編集モードか否か
    boolean isQuizEdit = "quiz".equals((String)request.getParameter("edit"));

    // 見出し
    String heading = isQuizEdit
        ? "科目を選択してください"
        : "科目一覧";

    // 説明文
    String explanation = isQuizEdit
        ? "問題を編集します。"
        : "科目を編集します。";

    // 科目を追加ボタン の非表示
    String displaynone = (isQuizEdit)
        ? "style=\"display: none;\""
        : "";
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <div class="white-area">
                
                <h1><%= heading %></h1>
                <p><%= explanation %></p>

                <table>
                    <tr>
                        <th>ID</th><th>科目</th>
                    </tr>
                    
                    <% for (Subject subject : subjects) { 
                        int     id   = subject.id;
                        String  name = subject.name;
                        String  link = (isQuizEdit)
                            ? "quizlist?subjectid="    + id
                            : "subjectedit?subjectid=" + id;
                    %>
                        <tr>
                            <th><%= id %></td>
                            <td><a href="<%= link %>"><%= name %></a></td>
                        </tr>
                    <% } %>
                </table>

                <a class="button" href="subjectedit?subjectid=0" <%= displaynone %>>
                    科目を追加</a>
            </div>

            <a class="button red wide" href="top">戻る</a>
        </div>
    </body>
</html>