<%--
  Created by IntelliJ IDEA.
  User: huangyusi
  Date: 5/14/20
  Time: 1:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>请登录</title>
</head>
<body>

<div id="myBox">

    <h2>
        Welcome Amigo! 请登录
    </h2>

    <form action="/list">
        <fieldset>
            <label>用户名:</label>
            <input type="text" value="<c:out value="${account.name}" />" name="name" required="required">
        </fieldset>
        <fieldset>
            <label>密码:</label>
            <input type="text" value="<c:out value="${account.password}" />" name="password" required="required">
        </fieldset>

        <div align="right">
            <button type="submit">Sign In</button>
        </div>
    </form>
</div>

</body>
<style>
    #myBox {
        position: fixed;
        top: 35%;
        left: 50%;
        transform: translate(-50%, -50%);
        box-sizing: content-box;
        padding:10px 10px 0 10px;
    }
</style>
</html>
