<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Verification Result</title>
</head>
<body>
<h2>Verification Result</h2>
<c:choose>
    <c:when test="${verified}">
        <p>Your account has been successfully verified! You can now <a href="/login">log in</a>.</p>
    </c:when>
    <c:otherwise>
        <p>Invalid or expired verification code.</p>
    </c:otherwise>
</c:choose>
<a href="/home">Back to Home</a>
</body>
</html>