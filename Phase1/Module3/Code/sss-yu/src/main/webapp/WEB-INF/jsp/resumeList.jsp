<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>简历表全记录</title>
</head>
<body>

<div id="myBox">
    <div align="center">
        <h2>List of Resumes</h2>
    </div>

    <div align="right">
           <button><a href="/resume/edit">Add a new resume</a></button>
    </div>

    <br/>

    <table border="2" cellpadding="10">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Address</th>
            <th>Phone</th>
        </tr>
        <c:forEach var="resume" items="${myList}">
            <tr>
                <td><c:out value="${resume.id}" /></td>
                <td><c:out value="${resume.name}" /></td>
                <td><c:out value="${resume.address}" /></td>
                <td><c:out value="${resume.phone}" /></td>
                <td><a href="/resume/edit?id=<c:out value="${resume.id}"/>"> Edit </a></td>
                <td><a href="/resume/delete?id=<c:out value="${resume.id}"/>"> Delete </a></td>
            </tr>
        </c:forEach>
    </table>
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