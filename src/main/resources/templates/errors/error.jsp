<%--
  Created by IntelliJ IDEA.
  User: konon
  Date: 16.12.2024
  Time: 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="ru" >
<head>
    <meta charset="UTF-8">
    <title>Ошибка</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta2/css/all.min.css">
    <link rel="stylesheet" href="../../static/css/errorStyles.css">
</head>

<body>

<canvas id="canvas"></canvas>

<button class="back-button" onclick="history.back()">Назад</button>


<div class="error-container">
    <h1>Что-то пошло не так...<br>
        <%= response.getStatus() %>
    </h1>
    <p> <i class="fas fa-seedling"></i><br>
        Ваши деревья в безопасности. </p>
    <p><i class="fas fa-biohazard"></i><br>
        Скорее всего, в этом виноваты монстры.</p>
    <p><i class="fas fa-heartbeat"></i><br>
        Не волнуйтесь, мы уже стараемся это исправить. А пока вы можете поиграть с нашим ручным монстром. Его имя - "Монстро"</p>
    <button onclick="window.history.back()" class="error-button">Вернуться назад</button>
    <button class="error-close-button" id="closeErrorButton">Закрыть окно</button>
</div>


<script src="../../static/js/error.js"></script>
<script src="../../static/js/monstro.js"></script>

</body>

</html>


