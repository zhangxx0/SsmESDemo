<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/20
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DemoList</title>
</head>
<body>


<table>

    <thead>
        <tr>
            <td>id</td>
            <td>userName</td>
            <td>age</td>
            <td>sex</td>
            <td>birthday</td>
            <td>createDate</td>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${users}" var="item">
            <tr>
                <td>${item.id}</td>
                <td>${item.userName}</td>
                <td>${item.age}</td>
                <td>${item.sex.sexName}</td>
                <td><fmt:formatDate value="${item.birthday}" pattern="yyyy-MM-dd"/> </td>
                <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
        </c:forEach>
    </tbody>

</table>

</body>
</html>
