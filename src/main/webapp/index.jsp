<!DOCTYPE HTML>
<%@ page pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Repair Index</title>
</head>
<body>
    <h2>Проверка связи из GP3-servlet</h2>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="text" name="login"><br/>
        <input type="password" name="password"><br/><br/>
        <input class="button" type="submit" value="Submit">
    </form>
    <br />
    <a href="${pageContext.request.contextPath}/reg">Registration</a>
    <br />
    <a href="${pageContext.request.contextPath}/error">Exception</a>
    <br />
</body>
</html>
