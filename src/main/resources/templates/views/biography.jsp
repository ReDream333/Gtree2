<%
    String nodeName = (String) request.getAttribute("nodeName");
    Long nodeId =   (Long) request.getAttribute("nodeId");
    String biography = (String) request.getAttribute("biography");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Биография</title>
    <link rel="stylesheet" href="../../static/css/biography.css"> <!-- Подключаем CSS -->
    <link href="https://fonts.googleapis.com/css2?family=IM+Fell+English+SC&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body>

<header>
    <h1>Биография</h1>
    <button id="backButton" onclick="window.history.back()">Назад</button>
</header>

<main>
    <div id="bioContainer">
        <h2 id="personName"><%= nodeName %></h2>
        <label for="bioInput"></label>
        <textarea id="bioInput" placeholder="Напишите биографию..." rows="12"><%=biography%></textarea>

        <!-- Строка статуса -->
        <div id="statusBar">
            <span id="charCount">0 символов</span>
            <span id="saveStatus">Текст не сохранён</span>
        </div>
        <!-- Кнопки действий -->
        <div class="button-container">
            <button id="saveButton" class="action-button">
                <i class="fas fa-save"></i> Сохранить
            </button>
            <button id="clearButton" class="action-button">
                <i class="fas fa-trash"></i> Очистить
            </button>
            <button id="openScrollButton" class="scroll-button">
                <i class="fas fa-scroll"></i> Открыть летопись
            </button>
        </div>


        <!-- Летопись -->
        <div id="scrollContainer" class="scroll-container">
            <button id="closeScrollButton" class="close-scroll-button">&times;</button>
            <div id="scrollContent" class="scroll-content">
                <h2 class="scroll-title">Летопись</h2>
                <p id="scrollText" class="scroll-text">
                    Здесь будет красиво отображаться вся записанная информация о человеке в виде старинного письма...
                </p>
            </div>
        </div>

    </div>
</main>

<script src="../../static/js/biography.js"></script>
<script>
    const nodeId = "<%= nodeId %>";
</script>
</body>
</html>

