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
                
                <h1>${heading}</h1>
                <p>${paragraph}</p>

                <table>
                    <tr>
                        <th>ID</th><th>科目</th>
                    </tr>
                    <c:forEach var="subject" items="${subjects}">
                        <tr>
                            <th>${subject.id}</td>
                            <td>
                                <a href="${url}?subjectid=${subject.id}">
                                    ${subject.name}
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

                <a class="button" href="subjectedit?subjectid=0" 
                    <c:if test="${isQuizEdit}">style="display:none;"</c:if>
                >科目を追加</a>
            </div>

            <a class="button red wide" href="top">戻る</a>
        </div>
    </body>
</html>