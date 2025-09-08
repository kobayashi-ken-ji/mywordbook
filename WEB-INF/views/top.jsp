<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <a class="button" href="logout">ログアウト</a>

            <form class="white-area buttonlike" action="toquiz" method="get" name="form">
                <h1>出題設定</h1>

                <div>
                    科目<br>
                    <select id="subject-select" name="subjectid">

                        <c:forEach var="subject" items="${subjects}">
                            <option value='${subject.id}'>${subject.name}</option>
                        </c:forEach>

                    </select>
                </div>
                <br>

                <div>
                    出題範囲<br>

                    <c:forEach var="difficulty" items="${difficulties}">
                        <label>
                            <input
                                type="checkbox" name="difficultyids"
                                value="${difficulty.id}" 
                                <c:if test="${1 < difficulty.id}">checked</c:if>
                            >
                            <span>${difficulty.name}</span>
                        </label>
                    </c:forEach>
                </div>
                <br>

                <div>
                    出題方式<br>
                    <label>
                        <input type="radio" name="format" value="normal" checked>
                        <span>通常</span>
                    </label>
                    <label>
                        <input type="radio" name="format" value="swap">
                        <span>問題 ⇔ 答え</span>
                    </label>
                </div>
                <br>

                <button type="submit" class="red wide" onclick="topStartButton()">
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
        <script src="script/onclick.js" charset="utf-8"></script>
    </body>
</html>