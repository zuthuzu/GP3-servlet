<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<html>
<head>
    <title><fmt:message key="general.title" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/colorscheme.css">
</head>
<body>
<div class="container">
    <%@ include file="/WEB-INF/header-guest.jspf" %>

    <div class="row">
        <div class="col">
            <br />
            <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="login.error" />
                </div>
            </c:if>
            <c:if test="${param.denied != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="general.access_denied" />
                </div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="login.logout" />
                </div>
            </c:if>
            <c:if test="${param.reg != null}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="reg.success" />
                </div>
            </c:if>
        </div>
    </div>

    <div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="index.welcome" /></h3>
            <fmt:message key="index.intro" />
            <br /><br />
            <fmt:message key="index.sign-in-to-order" />
            <br /><br />
        </div>
    </div>

    <div class="row">
        <div class="col">
            <br />
        </div>
    </div>

    <%@ include file="/WEB-INF/footer.jspf" %>
</div>
<%@ include file="/WEB-INF/bootscripts.jspf" %>
</body>
</html>
