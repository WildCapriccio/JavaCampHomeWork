<%--
  Created by IntelliJ IDEA.
  User: huangyusi
  Date: 5/14/20
  Time: 12:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>填写信息</title>
</head>
<body>
Welcome! Amigo!
Your resume ID is: ${resume.id}
<br/>
Youre resume is: ${resume}
<br/>
<div id="myBox">

    <h2>
        <c:if test="${resume.id != null}">
            Edit the Resume
        </c:if>
        <c:if test="${resume.id == null}">
            Add a New Resume
        </c:if>
    </h2>

    <c:if test="${resume.id != null}">
        <form action="/resume/update">
            <input type="hidden" name="id" value="<c:out value="${resume.id}"/>" />
    </c:if>
    <c:if test="${resume.id == null}">
        <form action="/resume/add">
    </c:if>

            <fieldset>
                <label>Name:</label>
                <input type="text" value="<c:out value="${resume.name}" />" name="name" required="required">
            </fieldset>
            <fieldset>
                <label>Address:</label>
                <input type="text" value="<c:out value="${resume.address}" />" name="address" required="required">
            </fieldset>
            <fieldset>
                <label>Phone:</label>
                <input type="text" value="<c:out value="${resume.phone}" />" name="phone" required="required">
            </fieldset>

             <div align="right">
                <button type="submit">Save</button>
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
