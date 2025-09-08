<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.models.*" %>
<%
    Subject subject = (Subject)request.getAttribute("subject");
    boolean isNew = (subject.id <= 0);

    // 科目ID
    String id = (isNew)
        ? "- 新規作成 -"
        : String.valueOf(subject.id);

    // 削除ボタン、削除説明の非表示
    String displaynone = (isNew)
        ? "style=\"display: none;\""
        : "";
%>

<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <form class="white-area" action="subjectchange" method="post" name="form">

                <h1>科目編集</h1>

                <table class="td-align-left ">
                    <tr>
                        <th>ID</th>
                        <td><%= id %></td>
                    </tr>
                    <tr>
                        <th>科目名</th>
                        <td><input type="text" name="subjectname" value="<%= subject.name %>"></td>
                    </tr>
                </table>
                
                <input  type="hidden" name="subjectid" value="<%= subject.id %>">
                <button type="submit" name="button" value="save"   onclick="subjectSaveButton()">保存</button>
                <button type="submit" name="button" value="delete" onclick="subjectDeleteButton()" <%= displaynone %>>削除</button>
                
                <div <%= displaynone %>>
                    <hr>
                    <p class="destroy-alert" >
                        [ 削除時の注意点 ]<br>
                        科目内の問題も全て削除されます。
                    </p>
                </div>
            </form>
            
            <a class="button red wide" onclick="history.back()">キャンセル</a>
        </div>

        <script src="script/onclick.js" charset="utf-8"></script>
    </body>
</html>