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
                    <c:if test="${noSubjects}">
                        <p class="alert-red">
                            科目がありません。<br>
                            新しく作成してください。
                        </p>
                    </c:if>

                    <c:if test="${!noSubjects}">
                        <select id="subject-select" name="subjectid">

                            <c:forEach var="subject" items="${subjects}">
                                <option
                                    value='${subject.id}'
                                    ${subject.id == quizSetting.subjectId ? " selected" : ""}
                                >
                                    ${subject.name}
                                </option>
                            </c:forEach>

                        </select>
                    </c:if>
                </div>
                <br>
                                
                <div class="buttonlike devide4">
                    出題範囲<br>

                    <c:forEach var="difficulty" items="${difficultyMap}">
                        <label>
                            <input
                                type="checkbox" name="difficultyids"
                                value="${difficulty.key}" 
                                ${activeDifficultyIds.contains(difficulty.key) ? " checked" : "" }
                            >
                            <span>${difficulty.value}</span>
                        </label>
                    </c:forEach>
                </div>
                <br>

                <div>
                    出題方式<br>
                    <label>
                        <input type="radio" name="format" value="normal"
                            ${quizSetting.isSwapMode ? "" : " checked" }
                        >
                        <span>通常</span>
                    </label>
                    <label>
                        <input type="radio" name="format" value="swap"
                            ${quizSetting.isSwapMode ? " checked" : "" }
                        >
                        <span>問題 ⇔ 答え</span>
                    </label>
                </div>
                <br>

                <div>
                    一度に出題する問題数<br>
                    <select id="lot-size" name="lot-size">
                        <c:forEach var="lotSize" items="${lotSizes}">
                            <option
                                value='${lotSize}'
                                ${lotSize == quizSetting.lotSize ? " selected" : "" }
                            >
                                ${lotSize}問
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <br>

                <button
                    type="submit" class="wide ${isContinuable ? 'blue' : 'red'}"
                    name="action" value="new-start"
                    ${noSubjects ? " disabled" : "" }
                    onclick="topStartButton()"
                >
                    出題スタート
                </button>

                <c:if test="${isContinuable}">
                    <button
                        type="submit" class="wide red" name="action" value="continue"
                        ${noSubjects ? " disabled" : "" }
                    >
                        前回の続きから
                    </button>
                </c:if>
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