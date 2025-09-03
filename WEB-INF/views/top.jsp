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
            <form class="white-area buttonlike">

                <h1>出題設定</h1>

                <div>
                    科目<br>
                    <select id="subject-select">
                        <% for (Subject subject : subjects) { 
                            int     id   = subject.id;
                            String  name = subject.name;
                        %>
                            <option value='<%= id %>'><%= name %></option>
                        <% } %>
                    </select>
                </div>
                <br>


                <div>
                    出題範囲<br>
                    <label>
                        <input type="checkbox" name="scope" checked>
                        <span>難しい</span>
                    </label>
                    <label>
                        <input type="checkbox" name="scope" checked>
                        <span>普通</span>
                    </label>
                    <label>
                        <input type="checkbox" name="scope">
                        <span>簡単</span>
                    </label>
                </div>
                <br>

                <div>
                    出題方式<br>
                    <label>
                        <input type="radio" name="method" checked>
                        <span>通常</span>
                    </label>
                    <label>
                        <input type="radio" name="method">
                        <span>問題 ⇔ 答え</span>
                    </label>
                </div>
                <br>

                <button type="submit" class="red wide">
                    出題スタート
                </button>
                <br>
            </form>
            <br>
                

            <div>
                <a href="subjectlist?edit=subject">科目を編集</a> | 
                <a href="subjectlist?edit=quiz"   >問題を編集</a>
            </div>
        </div>
    </body>
</html>