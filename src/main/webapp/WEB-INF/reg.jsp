<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<html>
<head>
    <title><fmt:message key="general.title" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/colorscheme.css">
</head>
<body onload="checkFields()">
<div class="container">
    <%@ include file="/WEB-INF/header-guest.jspf" %>

    <div class="row">
        <div class="col">
            <br />
            <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="reg.error.generic" />
                </div>
            </c:if>
            <c:if test="${param.duplicate != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="reg.error.duplicate" />
                </div>
            </c:if>
        </div>
    </div>

    <div class="row bg-light">
        <div class="col">
            <h3><fmt:message key="reg.welcome" /></h3>
            <fmt:message key="reg.header" />
            <br /><br />
            <div>
                <form id="newUser" name="newUser" action="${pageContext.request.contextPath}/app/newuser" method="post">
                    <div class="form-group">
                        <label for="name">
                            <fmt:message key="reg.name" />
                            <span id="nameYet">🗅</span>
                            <span id="nameCheck">✅</span>
                            <span id="nameFail">❌</span>
                        </label>
                        <input class="form-control" type="text" id="name" name="name" value="${name}"
                            placeholder="<fmt:message key="reg.name" />" required>
                    </div>
                    <div class="form-group">
                        <label for="login">
                            <fmt:message key="login.login" />
                            <span id="loginYet">🗅</span>
                            <span id="loginCheck">✅</span>
                            <span id="loginFail">❌</span>
                        </label>
                        <input class="form-control" type="text" id="login" name="login" value="${login}"
                            placeholder="<fmt:message key="login.login" />" required>
                    </div>
                    <div class="form-group">
                        <label for="phone">
                            <fmt:message key="reg.phone" />
                            <span id="phoneYet">🗅</span>
                            <span id="phoneCheck">✅</span>
                            <span id="phoneFail">❌</span>
                        </label>
                        <input class="form-control" type="text" id="phone" name="phone" value="${phone}"
                            placeholder="<fmt:message key="reg.phone" />" required>
                    </div>
                    <div class="form-group">
                        <label for="email">
                            <fmt:message key="reg.email" />
                            <span id="emailYet">🗅</span>
                            <span id="emailCheck">✅</span>
                            <span id="emailFail">❌</span>
                        </label>
                        <input class="form-control" type="email" id="email" name="email" value="${email}"
                            placeholder="<fmt:message key="reg.email" />">
                    </div>
                    <div class="form-group">
                        <label for="password"><fmt:message key="login.password" /></label>
                        <input class="form-control" type="password" id="password" name="password"
                            placeholder="<fmt:message key="login.password" />" required>
                    </div>
                    <div>
                        <input class="btn btn-secondary" type="submit" value="<fmt:message key="reg.submit" />">
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

<script type="text/javascript" th:inline="javascript">
    var inputFieldName = document.forms["newUser"]["name"];
    var inputFieldLogin = document.forms["newUser"]["login"];
    var inputFieldPhone = document.forms["newUser"]["phone"];
    var inputFieldEmail = document.forms["newUser"]["email"];
    var inputFieldPassword = document.forms["newUser"]["password"];

    var nameRegex = /${nameRegex}/;
    var loginRegex = /${loginRegex}/;
    var phoneRegex = /${phoneRegex}/;

    inputFieldName.addEventListener("blur", function () {
        checkField(inputFieldName, nameRegex)
    }, false)

    inputFieldLogin.addEventListener("blur", function () {
        checkField(inputFieldLogin, loginRegex)
    }, false)

    inputFieldPhone.addEventListener("blur", function () {
        checkField(inputFieldPhone, phoneRegex)
    }, false)

    inputFieldEmail.addEventListener("blur", function () {
        checkEmail(inputFieldEmail)
    }, false)

    function checkFields(){
        checkField(inputFieldName, nameRegex);
        checkField(inputFieldLogin, loginRegex);
        checkField(inputFieldPhone, phoneRegex);
        checkEmail(inputFieldEmail);
    }

    function checkField(field, constraint) {
        regex = new RegExp(constraint);
        var result = regex.test(field.value);
        setMarks(field, result);
    }

    function checkEmail(field) {
        setMarks(field, field.validity.valid);
    }

    function setMarks(field, valid) {
        yetmark = document.getElementById(field.name + "Yet")
        checkmark = document.getElementById(field.name + "Check");
        failmark = document.getElementById(field.name + "Fail");

        if (field.value != undefined && field.value.length == 0) {
            yetmark.style.display = "inline";
            checkmark.style.display = "none";
            failmark.style.display = "none";
        } else if (valid) {
            yetmark.style.display = "none";
            checkmark.style.display = "inline";
            failmark.style.display = "none";
        }   else {
            yetmark.style.display = "none";
            checkmark.style.display = "none";
            failmark.style.display = "inline";
        }
    }
</script>
<%@ include file="/WEB-INF/bootscripts.jspf" %>
</body>
</html>
