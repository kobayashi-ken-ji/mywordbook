<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <div class="white-area">

                <h1>${heading}</h1>
                <p>${paragraph}</p>

            </div>
            <a class="button red wide" href="${url}">${buttonname}</a>
        </div>
    </body>
</html>