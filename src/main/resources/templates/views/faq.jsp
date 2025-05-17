<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Вопросы</title>
    <link rel="stylesheet" href="../../static/css/forHome.css">
</head>

<body>

<jsp:include page="../base/header.jsp"/>

<nav>
    <a href="${pageContext.request.contextPath}/register">Регистрация</a>
    <a href="home.jsp">Главная</a>
    <a href="${pageContext.request.contextPath}/sing.jsp">Вход</a>
</nav>

<div class="container">

    <div class="intro">
        <h1>Добро пожаловать на сайт для создания генеалогического древа!</h1>
        <p>Сохраняйте и изучайте историю вашей семьи <br/>
            Ниже вы можете ознакомиться с часто возникающими вопросами</p>
    </div>

    <jsp:include page="../base/tree-steps.jsp"/>

    <div class="faq-item">
        <p class="faq-question">Как создать генеалогическое древо?</p>
        <p class="faq-answer">Просто зарегистрируйтесь и начните добавлять своих предков.</p>
    </div>

    <div class="faq-item">
        <p class="faq-question">Это бесплатно?</p>
        <p class="faq-answer">Да, ко всем материалам на платформе вы имеете бесплатный доступ. Это наша принципиальная
            позиция: мы хотим сделать наш сервис и саму генеалогию популярной, объяснить, как важны традиции сохранения
            семейной памяти и ценностей.</p>
    </div>

    <div class="faq-item">
        <p class="faq-question">Я могу удалить страницу о себе или о своих прямых родственниках?</p>
        <p class="faq-answer">Конечно, также вы можете удалить свою учетную запись, если захотите. В таком случае
            удалятся все ваши деревья</p>
    </div>

    <div class="faq-item">
        <p class="faq-question">А если мне не нравится выставлять напоказ историю своей семьи? Могу ли я скрыть свое
            дерево?</p>
        <p class="faq-answer">Да, вы можете сделать его приватным.</p>
    </div>

    <div class="faq-item">
        <p class="faq-question">Что такое публичный доступ?</p>
        <p class="faq-answer">Это возможность делиться своим деревом с другими.</p>
    </div>

    <div class="faq-item">
        <p class="faq-question">Есть ли мобильное приложение?</p>
        <p class="faq-answer">Нет, но мы стараемся над этим!</p>
    </div>

    <div class="faq-item">
        <p class="faq-question">Как происходит слияние деревьев?</p>
        <p class="faq-answer">Извините, данная фича пока не работает(</p>
<%--        Вы отправляете запрос автору дерева на подтверждение слияния по общему предку. Если автор согласен, то ваши
            деревья соединяются! Конечно при условии, что оба дерева в открытом доступе.--%>
    </div>

</div>

<script>

    document.querySelectorAll('.faq-item').forEach(item => {
        const question = item.querySelector('.faq-question');

        question.addEventListener('click', () => {
            if (item.classList.contains('active')) {
                item.classList.remove('active');
            } else {
                document.querySelectorAll('.faq-item').forEach(i => i.classList.remove('active'));
                item.classList.add('active');
            }
        });
    });


</script>

<jsp:include page="../base/footer.jsp"/>

</body>

</html>
