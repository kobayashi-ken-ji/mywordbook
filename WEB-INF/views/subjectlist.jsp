<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.Subject" %>
<%
    // 科目ID, 科目名
    List<Subject> subjects = (List<Subject>)request.getAttribute("subjects");
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <div class="white-area">
                
                <h1>科目一覧</h1>
                <p>科目を編集します。</p>

                <table>
                    <tr>
                        <th>ID</th><th>科目</th>
                    </tr>
                    
                    <% for (Subject subject : subjects) { 
                        int     id   = subject.id;
                        String  name = subject.name;
                    %>
                        <tr>
                            <th><%= id %></td>
                            <td><a href=""><%= name %></a></td>
                        </tr>
                    <% } %>
                </table>

                <a class="button">科目を追加</a>
            </div>

            <a class="button red wide"  href="top">戻る</a>
        </div>
    </body>
</html>