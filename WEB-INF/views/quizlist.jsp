<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">
            <div class="white-area">

                <h1>${subject.name}</h1>
                <p>問題数 : ${quizzes.size()}</p>

                <a class="button" href="quizedit?id=0&subjectid=${subject.id}">
                    問題を追加</a>
                <br><br>

                <div class="search-box" style="display:none">
                    <input type="text" placeholder="ID、単語など">
                    <button>検索</button>
                </div>

                <table class="td-align-left">
                    <tr>
                        <th>ID</th><th>問題文</th>
                    </tr>

                    <c:forEach var="quiz" items="${quizzes}">
                        <tr>
                            <th>${quiz.id}</td>
                            <td>
                                <a href="quizdetails?quizid=${quiz.id}&state=normal">
                                    ${quiz.question}
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

            </div>

            <a class="button red wide" href="subjectlist?edit=quiz">戻る</a>
        </div>
    </body>
</html>