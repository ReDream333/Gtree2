
<%
  String nodes = (String) request.getAttribute("nodes");
  String links = (String) request.getAttribute("links");
  Long treeId = Long.valueOf(request.getParameter("treeId"));
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
  <script src="https://unpkg.com/gojs"></script>
    <script src="https://cdn.jsdelivr.net/npm/canvas-confetti@1.3.2"></script>
    <title>
    Диаграмма дерева
  </title>
    <link rel="stylesheet" href="../../static/css/treeDiagram.css">
</head>
<body>

<button class="back-button" onclick="location.href='/profile'">Назад</button>

<div id="sample">
  <div id="myDiagramDiv" ></div>
</div>

<div id="controls" style="margin-top: 10px;">
  <div>
    <span id="selectedNodeInfo"></span>
  </div>

</div>


<div id="nodeInfoPanel">
  <button id="closePanelButton" style="float: right;">✖</button>

  <!-- Фото в кружочке перед именем -->
  <div style="display: flex; align-items: center;">
    <img id="nodeViewPhoto" src="../../static/images/ava.jpg" alt="Фото" style="width: 100px; height: 100px; border-radius: 50%; margin-right: 10px;">
    <h2 id="nodeName">ФИО</h2>
  </div>

  <!-- Режим просмотра -->
  <div id="nodeViewMode">
    <p>Дата рождения: <span id="nodeBirthDate">Неизвестно</span></p>
    <p>Дата смерти: <span id="nodeDeathDate">Неизвестно</span></p>

    <button id="editNodeButton">Редактировать</button>
    <button id="biographyButton">Биография</button>
    <button id="photoAlbumButton">Фото-альбом</button>
    <button id="addParentButton" disabled>Добавить родителя</button>
    <button id="deleteNodeButton">Удалить</button>
  </div>

  <!-- Режим редактирования -->
  <div id="nodeEditMode">
    <label>Фамилия: <input type="text" id="editLastName"></label><br>
    <label>Имя: <input type="text" id="editFirstName"></label><br>
    <label>Дата рождения: <input type="date" id="editBirthDate"></label><br>
    <label>Дата смерти: <input type="date" id="editDeathDate"></label><br>

    <button id="upload_widget">Загрузить фото</button>
    <button id="saveNodeButton">Сохранить</button>
    <button id="cancelEditButton">Отмена</button>
  </div>


  <script src="https://upload-widget.cloudinary.com/latest/global/all.js" type="text/javascript"></script>


</div>

<div id="parentModal">
    <div>
        <h3>Добавить родителя</h3>
        <label>Имя: <input type="text" id="parentFirstName" placeholder="Введите имя"></label>
        <label>Фамилия: <input type="text" id="parentLastName" placeholder="Введите фамилию"></label>
        <label>Пол:
            <select id="parentGender">
                <option value="M">мужской</option>
                <option value="F">женский</option>
            </select>
        </label>
        <label>Дата рождения: <input type="date" id="parentBirthDate"></label>
        <label>Дата смерти: <input type="date" id="parentDeathDate"></label>
        <label>Биография: <textarea id="parentBiography" rows="3" placeholder="Введите биографию"></textarea></label>

        <label id="errorMessage">
            Имя, фамилия и пол обязательны для заполнения.
        </label>
        <button id="saveParentButton">Сохранить</button>
        <button id="cancelParentButton">Отмена</button>
    </div>
</div>

<div id="congratsModal" class="modal" style="display: none;">
    <div class="modal-content">
        <h2>🎉 Поздравляем! 🎉</h2>
        <p>Только что вы добавили самую главную ноду – <b>себя</b>.<br>
            Следующая ваша задача – взращивать дерево.<br>
            Добавляйте своих родителей и родственников!
        </p>
        <button id="closeModalButton" class="modal-button">Круто!</button>
    </div>
</div>



<script>
  const nodeDataArray = <%= nodes %>;
  const linkDataArray = <%= links %>;
  const treeId = <%= treeId %>;
</script>

<script src="../../static/js/congrats.js"></script>
<script src="../../static/js/treeDiagram.js"></script> <!-- Создаёт диаграмму -->
<script src="../../static/js/nodeInfo.js"></script>    <!-- Добавляет функционал нод -->

</body>
</html>
