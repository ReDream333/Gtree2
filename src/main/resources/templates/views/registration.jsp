<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" href="../../static/css/forHome.css">
    <link rel="stylesheet" href="../../static/css/regSing.css">

</head>
<body>

<div class="registration-container">
    <h2>Регистрация</h2>
    <form action="${pageContext.request.contextPath}/register" method="POST">

        <div class="form-group">
            <label for="login">Никнейм</label>
            <input type="text" id="login" name="login" required>

            <label for="email">Электронная почта</label>
            <input type="email" id="email" name="email" required>

            <label for="password">Пароль</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit" class="submit-btn">Зарегистрироваться</button>

    </form>
    <a href="sign-in.jsp" class="link">Уже зарегистрированы?</a>
</div>

<script src="../../static/js/register.js"></script>

</body>
</html>
