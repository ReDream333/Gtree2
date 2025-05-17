<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Главная</title>
    <link rel="stylesheet" href="../../static/css/forHome.css">
</head>

<body>

<jsp:include page="../base/header.jsp"/>

<nav>
    <a href="home.jsp">Главная</a>
    <a href="faq.jsp">FAQ</a>
    <a href="${pageContext.request.contextPath}/sing">Вход</a>
</nav>

<div class="container">
    <div class="intro">
        <h1>Добро пожаловать на сайт для создания генеалогического древа!</h1>
        <p>Здесь вы можете сохранить или узнать историю своей семьи. <br/>
            Кликнув на кнопку ниже, вы сможете пройти регистрацию. Если у вас уже есть аккаунт, перейдите на вкладку "Вход"</p>
        <jsp:include page="../base/tree-steps.jsp"/>
        <button onclick="location.href='/register'">Начать создание древа</button>
    </div>
</div>

<jsp:include page="../base/footer.jsp"/>

</body>

</html>

