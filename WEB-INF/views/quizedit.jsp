<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">
            <form class="white-area" action="quizupdate" method="post" name="form">

                <h1>問題編集</h1>

                <table class="td-align-left ">
                    <tr>
                        <th>ID</th>
                        <td>
                            ${(quiz.id == 0)
                                ? "- 新規作成 -"
                                : quiz.id
                            }
                        </td>
                    </tr>
                    <tr>
                        <th>科目</th>
                        <td>
                            <select id="subject-select" name="subjectid">
                                
                                <c:forEach var="subject" items="${subjects}">
                                    <option
                                        value='${subject.id}'
                                        ${subject.id == quiz.subjectId ? "selected" : "" }
                                    >
                                        ${subject.name}
                                    </option>
                                </c:forEach>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>問題文</th>
                        <td><input type="text" name="question" value="${quiz.question}" required></td>
                    </tr>
                    <tr>
                        <th>正解文</th>
                        <td><input type="text" name="answer" value="${quiz.answer}" required></td>
                    </tr>
                    <tr>
                        <th>説明文</th>
                        <td><textarea name="explanation">${quiz.explanation}</textarea></td>
                    </tr>
                    <tr>
                        <th>難易度</th>
                        <td  class="buttonlike devide4">

                            <c:forEach var="difficulty" items="${difficultyMap}">
                                <label>
                                    <input
                                        type="radio" name="difficultyid"
                                        value="${difficulty.key}" 
                                        ${difficulty.key == quiz.difficultyId ? "checked" : "" }
                                    >
                                    <span>${difficulty.value}</span>
                                </label>
                            </c:forEach>

                        </td>
                    </tr>
                </table>
                <br>
                
                <input  type="hidden" name="quizid" value="${quiz.id}">
                <button type="submit">保存</button>
                <a class="button red" href="${cancelURL}">キャンセル</a>
            </form>
        </div>
    </body>
</html>