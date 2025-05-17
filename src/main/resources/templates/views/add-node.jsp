<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Первая нода</title>
    <link rel="stylesheet" href="../../static/css/createTree.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>

</head>
<body>
<div id="particles-js"></div>
<div class="form-container">
    <h1>Введите свои данные</h1>
    <form action="${pageContext.request.contextPath}/addInitialNode" method="post" class="tree-form">
        <div class="form-group">
            <label for="firstName">Имя:</label>
            <input type="text" id="firstName" name="firstName" placeholder="Введите имя" required>
        </div>

        <div class="form-group">
            <label for="lastName">Фамилия:</label>
            <input type="text" id="lastName" name="lastName" placeholder="Введите фамилию" required>
        </div>

        <div class="form-group">
            <label for="gender">Пол:</label>
            <select id="gender" name="gender" required>
                <option value="M">Мужской</option>
                <option value="F">Женский</option>
            </select>
        </div>

        <div class="form-group">
            <label for="birthDate">Дата рождения:</label>
            <input type="date" id="birthDate" name="birthDate">
        </div>

        <div class="form-group">
            <label for="deathDate">Дата смерти:</label>
            <input type="date" id="deathDate" name="deathDate">
        </div>

        <div class="form-group">
            <label for="comment">Комментарий:</label>
            <textarea id="comment" name="comment" placeholder="Добавьте комментарий"></textarea>
        </div>

        <div class="button-container">
            <button type="submit" class="submit-button">Сохранить</button>
            <button type="button" class="cancel-button" onclick="window.history.back()">Отмена</button>
        </div>
    </form>
</div>

<script src="../../static/js/leafFall.js"></script>

</body>
</html>
