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
        </div>
    </div>

    <div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="order.details.welcome" /></h3>
            <div>
                <form id="updateOrder" name="updateOrder" action="${pageContext.request.contextPath}/app/updateorder" method="post">
                    <input type="hidden" id="id" name="id" value="${id}">
                    <input type="hidden" id="archived" name="archived" value="${archived}">
                    <div class="form-group">
                        <label for="date"><fmt:message key="order.creation_date" /></label>
                        <input class="form-control" id="date" name="date" placeholder="<fmt:message key="order.creation_date" />"
                        value="${creation_date}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="date"><fmt:message key="order.author" /></label>
                        <input class="form-control" id="author" name="author" placeholder="<fmt:message key="order.none" />"
                        value="${author}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="date"><fmt:message key="order.manager" /></label>
                        <input class="form-control" id="manager" name="manager" placeholder="<fmt:message key="order.none" />"
                        value="${manager}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="date"><fmt:message key="order.master" /></label>
                        <input class="form-control" id="master" name="master" placeholder="<fmt:message key="order.none" />"
                        value="${master}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="category"><fmt:message key="order.category" /></label>
                        <select class="form-control" type="text" id="category" name="category" value="${category}"
                        required <c:if test="${!available.contains('category')}">disabled</c:if>>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat}" <c:if test="${category.equals(cat)}">selected</c:if>>${cat}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="item"><fmt:message key="order.item" /></label>
                        <input class="form-control" type="text" id="item" name="item" placeholder="<fmt:message key="order.item" />"
                        value="${item}" required <c:if test="${!available.contains('item')}">disabled</c:if>>
                    </div>
                    <div class="form-group">
                        <label for="complaint"><fmt:message key="order.complaint" /></label>
                        <textarea class="form-control" id="complaint" name="complaint" placeholder="<fmt:message key="order.complaint" />"
                        rows="2"<c:if test="${!available.contains('complaint')}">disabled</c:if>>${complaint}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="status"><fmt:message key="order.price" /></label>
                        <input class="form-control" id="price" name="price" placeholder="<fmt:message key="order.none" />"
                        type="number" min="0" value="${price}" required <c:if test="${!available.contains('price')}">disabled</c:if>>
                    </div>
                    <div class="form-group">
                        <label for="status"><fmt:message key="order.status" /></label>
                        <input class="form-control" id="status" name="status" placeholder="<fmt:message key="order.status" />"
                        value="${status}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="managerComment"><fmt:message key="order.manager_comment" /></label>
                        <textarea class="form-control" id="managerComment" name="managerComment" rows="2"
                        <c:if test="${!available.contains('manager_comment')}">disabled</c:if>>${manager_comment}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="masterComment"><fmt:message key="order.master_comment" /></label>
                        <textarea class="form-control" id="masterComment" name="masterComment" rows="2"
                        <c:if test="${!available.contains('master_comment')}">disabled</c:if>>${master_comment}</textarea>
                    </div>
                    <c:if test="${archived}">
                    <div class="form-group">
                        <div><fmt:message key="order.user_stars" /></div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="userStars" id="star1" value="1">
                            <label class="form-check-label" for="star1">1</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="userStars" id="star2" value="2">
                            <label class="form-check-label" for="star2">2</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="userStars" id="star3" value="3">
                            <label class="form-check-label" for="star3">3</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="userStars" id="star4" value="4">
                            <label class="form-check-label" for="star4">4</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="userStars" id="star5" value="5>
                            <label class="form-check-label" for="star5">5</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userComment"><fmt:message key="order.user_comment" /></label>
                        <textarea class="form-control" id="userComment" name="userComment" rows="2"
                        <c:if test="${!available.contains('user_comment')}">disabled</c:if>>${user_comment}</textarea>
                    </div>
                    </c:if>
                    <div>
                        <c:if test="${proceed}"><button class="btn btn-secondary" type="submit" name="action" value="proceed">
                            ${submit}
                        </button></c:if>
                        <c:if test="${cancel}"><button class="btn btn-secondary" type="submit" name="action" value="cancel">
                            <fmt:message key="order.action.cancel" />
                        </button></c:if>
                        <button class="btn btn-secondary" type="button" onclick="location.href='${pageContext.request.contextPath}/lobby'">
                            <fmt:message key="order.action.lobby" />
                        </button>
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
