<%--<jsp:useBean id="user" scope="session" type="ru.kpfu.itis.kononenko.entity.User"/>--%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль пользователя</title>
    <link rel="stylesheet" href="../../static/css/profile.css">

</head>


<body>

<div class="user-container">
    <img src="${user.photo() != null && user.photo() != ""? user.photo() : 'images/sunf.jpg'}"
         alt="Фото пользователя" class="user-photo">

    <div class="user-info-section">
        <div class="user-details">
            <h2>${user.username()}</h2>
            <p>Дата регистрации: ${user.createdAt()}</p>
        </div>

        <div class="tree-buttons">
            <button class="button" onclick="location.href='/userTrees'">Просмотреть свои деревья</button>
            <button class="button" onclick="location.href='/publicTrees'">Просмотреть чужие деревья</button>
            <form action="${pageContext.request.contextPath}/createTree" method="GET">
                <button type="submit" class="button">Создать новое дерево</button>
            </form>



        </div>
    </div>

    <div class="user-buttons">
        <button class="edit-profile-button" onclick="location.href='/change'">Изменить профиль</button>
        <button id="upload_widget" class="edit-profile-button">Изменить фото профиля</button>
        <button class="logout-button" onclick="location.href='/logout'">Выход</button>
        <button id="delete-button" class="delete-button">Удалить аккаунт</button>
    </div>
</div>



<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>

<script src="https://upload-widget.cloudinary.com/latest/global/all.js" type="text/javascript"></script>
<script src="../../static/js/profile.js" defer></script>




</body>
</html>
