<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" href="../../static/css/forHome.css">
    <link rel="stylesheet" href="../../static/css/regSing.css">

</head>
<body>

<div class="registration-container">
    <h2>Вход</h2>
    <form id="login-form">

        <div class="form-group">
            <label for="login">Логин</label>
            <input type="text" id="login" name="login" required>

            <label for="password">Пароль</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div id="error-message"></div>

        <button type="submit" class="submit-btn">Войти</button>
    </form>

    <a href="${pageContext.request.contextPath}/register" class="link">Еще не с нами? Зарегистрируйтесь</a>
</div>

</body>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>

<script src="../../static/js/sing.js" defer></script>

<style>
    #error-message {
        color: red;
        margin-top: 10px;
        font-size: 14px;
    }
</style>
</html>
