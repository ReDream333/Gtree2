<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Создание дерева</title>
    <link rel="stylesheet" href="../../static/css/createTree.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>

</head>

<body>
<div id="particles-js"></div>
<div class="form-container">
    <h1>Создание нового дерева</h1>
    <form action="${pageContext.request.contextPath}/createTree" method="POST" class="tree-form">
        <div class="form-group">
            <label for="treeName">Название дерева:</label>
            <input type="text" id="treeName" name="treeName" placeholder="Введите название дерева" required>
        </div>

        <div class="form-group">
            <label for="isPrivate">Приватное дерево?</label>
            <select id="isPrivate" name="isPrivate">
                <option value="true">Да</option>
                <option value="false">Нет</option>
            </select>
        </div>

        <div class="button-container">
            <button type="submit" class="submit-button">Создать</button>
            <button type="button" class="cancel-button" onclick="location.href='/profile'">Отмена</button>
        </div>
    </form>
</div>

<script src="../../static/js/leafFall.js"></script>
</body>


</html>
