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
                    <fmt:message key="order.error.generic" />
                </div>
            </c:if>
        </div>
    </div>

    <div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="order.new.welcome" /></h3>
            <div>
                <form id="newOrder" name="newOrder" action="${pageContext.request.contextPath}/app/neworder" method="post">
                    <div class="form-group">
                        <label for="category"><fmt:message key="order.category" /></label>
                        <select class="form-control" type="text" id="category" name="category" value="${category}" required>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat}">${cat}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="item"><fmt:message key="order.item" /></label>
                        <input class="form-control" type="text" id="item" name="item" placeholder="<fmt:message key="order.item" />"
                        required value="${item}">
                    </div>
                    <div class="form-group">
                        <label for="complaint"><fmt:message key="order.complaint" /></label>
                        <textarea class="form-control" id="complaint" name="complaint" placeholder="<fmt:message key="order.complaint" />"
                        required value="${complaint}"></textarea>
                    </div>
                    <div>
                        <input class="btn btn-secondary" type="submit" value="<fmt:message key="order.new.submit" />">
                    </div>
                </form>
            </div>
            <br />
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
