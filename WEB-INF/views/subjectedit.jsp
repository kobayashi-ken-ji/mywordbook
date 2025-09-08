<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
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
                        <td>${subject.idString}</td>
                    </tr>
                    <tr>
                        <th>科目名</th>
                        <td><input type="text" name="subjectname" value="${subject.name}"></td>
                    </tr>
                </table>
                
                <input  type="hidden" name="subjectid" value="${subject.id}">
                <button type="submit" name="button" value="save"   onclick="subjectSaveButton()">保存</button>
                <button type="submit" name="button" value="delete" onclick="subjectDeleteButton()"
                    <c:if test="${isNew}">style="display:none;"</c:if>>削除</button>
                
                <c:if test="${!isNew}">
                    <div>
                        <hr>
                        <p class="destroy-alert" >
                            [ 削除時の注意点 ]<br>
                            科目内の問題も全て削除されます。
                        </p>
                    </div>
                </c:if>
            </form>
            
            <a class="button red wide" onclick="history.back()">キャンセル</a>
        </div>

        <script src="script/onclick.js" charset="utf-8"></script>
    </body>
</html>