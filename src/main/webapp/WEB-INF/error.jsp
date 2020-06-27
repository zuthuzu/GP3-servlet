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
    <div class="row bg-light">
        <div class="col-sm-8">
            <a class="text-dark" href="${pageContext.request.contextPath}/"><h1><fmt:message key="header.brand" /></h1></a>
        </div>
        <div class="col p-2 d-flex justify-content-end">
            <div class="dropdown show">
                <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <fmt:message key="header.language" />
                </button>

                <div class="dropdown-menu">
                    <c:forEach items="${supported}" var="current">
                        <a class="dropdown-item" href="?l=${current.code}">${current.name}</a>
                    </c:forEach>
                </div>
            </div>

            <div class="p-2">

            </div>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <br />
            <div class="alert alert-danger" role="alert">
                <h3><fmt:message key="error.welcome" /></h3>
                <h3>${status_code}</h3>
                ${exception}
            </div>
        </div>
    </div>

    <div class="row bg-light">
        <div class="col">
            <br />
            <fmt:message key="error.general" />
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