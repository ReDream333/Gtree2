<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои Деревья</title>
    <link rel="stylesheet" href="../../static/css/userTrees.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">


</head>

<body>
<button class="back-button" onclick="history.back()">Назад</button>


<div class="user-container">
    <h2>Мои Деревья</h2>

    <div class="tree-buttons">

        <jsp:useBean id="userTrees" scope="request" type="java.util.List"/>
        <c:if test="${empty userTrees}">
            <p class="no-trees-message">У вас еще нет созданных деревьев.</p>
        </c:if>

        <c:forEach var="tree" items="${userTrees}">
            <button class="tree-button"
                    onclick="location.href='${pageContext.request.contextPath}/viewTree?treeId=${tree.id()}'">
                    ${tree.name()} <i class="fa-solid fa-tree"></i>
            </button>
        </c:forEach>
    </div>
</div>



</body>
</html>
