<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Изменение данных пользователя</title>
    <link rel="stylesheet" href="../../static/css/profileSettings.css">
</head>

<body>
<div class="settings-container">
    <h1>Настройки профиля</h1>
    <form id="updateForm">
        <label for="newName">Новое имя:</label>
        <input type="text" id="newName" name="newName" placeholder="Введите новое имя">

        <label for="newPassword">Новый пароль:</label>
        <input type="password" id="newPassword" name="newPassword" placeholder="Введите новый пароль">

        <button type="button" id="updateButton">Сохранить изменения</button>
        <button class="back-button" onclick="location.href='/profile'">Назад</button>
    </form>
    <p id="message" style="color: red;"></p>
</div>

<div id="confirmationModal" class="modal">
    <div class="modal-content">
        <h2>Подтверждение</h2>
        <label for="currentNameInput">Текущий логин:</label>
        <input type="text" id="currentNameInput" placeholder="Введите текущий логин">

        <label for="currentPassword">Текущий пароль:</label>
        <input type="password" id="currentPassword" placeholder="Введите текущий пароль">

        <button id="confirmButton">Подтвердить</button>
        <button id="closeModalButton">Отмена</button>
    </div>
</div>

<script src="../../static/js/profileSettings.js"></script>
</body>
</html>
