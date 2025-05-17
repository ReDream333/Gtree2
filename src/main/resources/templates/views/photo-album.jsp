<%
    String nodeName = (String) request.getAttribute("nodeName");
    String photos = (String) request.getAttribute("photos");
    String nodeId = request.getParameter("nodeId");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Фотоальбом</title>
    <link rel="stylesheet" href="../../static/css/photoAlbum.css"> <!-- Подключим CSS -->
    <script src="https://upload-widget.cloudinary.com/latest/global/all.js" type="text/javascript"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body>
<header>
    <div class="header-left">
        <h1 id="albumTitle">Фотоальбом</h1>
        <span id="familyMemberName">
            <%= nodeName %>
        </span>
    </div>

    <button id="backButton" onclick="window.history.back()">
        Назад
    </button>
    <button id="uploadPhotoButton" class="cloudinary-button">Добавить фото</button>

</header>



<div id="photoAlbum" style="display: flex; flex-wrap: wrap; gap: 20px; justify-content: center;">
</div>

<div id="lightbox" class="lightbox" style="display: none;">
    <span id="closeLightbox" class="close">&times;</span>
    <img id="lightboxImage" class="lightbox-content" src="" alt="Full Image">
</div>

<script>
    let photos = <%= photos %>;
    const nodeId = <%= nodeId %>;
</script>

<script src="../../static/js/photoAlbum.js"></script>
</body>
</html>
