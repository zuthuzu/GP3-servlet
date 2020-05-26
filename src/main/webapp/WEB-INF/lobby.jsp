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
    <%@ include file="/WEB-INF/header-user.jspf" %>

    <div class="row">
        <div class="col">
            <br />
            <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="lobby.order.error" />
                </div>
            </c:if>
            <c:if test="${param.denied != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="general.access_denied" />
                </div>
            </c:if>
            <c:if test="${param.success != null}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="lobby.order.saved" />
                </div>
            </c:if>
        </div>
    </div>

    <c:if test="${canPlaceNew}"><div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="lobby.welcome" /></h3>
            📥 <a href="${pageContext.request.contextPath}/order"><fmt:message key="lobby.order.place" /></a>
            <br /><br />
        </div>
    </div></c:if>

    <div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="lobby.order.active" /></h3>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th><fmt:message key="order.creation_date" /></th>
                    <th><fmt:message key="order.item" /></th>
                    <th><fmt:message key="order.status" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${activeOrders}" var="order">
                <tr onclick="window.location.href='${pageContext.request.contextPath}/details?id=${order.id}'">
                    <td>${order.creationDate}</td>
                    <td>${order.item}</td>
                    <td>${order.status}</td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <br />
        </div>
    </div>

    <div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="lobby.order.secondary" /></h3>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th><fmt:message key="order.creation_date" /></th>
                    <th><fmt:message key="order.item" /></th>
                    <th><fmt:message key="order.status" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${secondaryOrders}" var="order">
                <tr onclick="window.location.href='${pageContext.request.contextPath}/details?id=${order.id}'">
                    <td>${order.creationDate}</td>
                    <td>${order.item}</td>
                    <td>${order.status}</td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
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
