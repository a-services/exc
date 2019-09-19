<%@ page import="com.exadel.exc.*, java.text.*, java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<%
String configName  = (String) request.getAttribute("configName");
String configText  = (String) request.getAttribute("configText");
%>
<html>

<head>
    <title>Configuration</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style><%@include file="exc.css"%></style>
</head>

<body>
    <h3>Configuration</span></h3>
    <p>
        <div class="indent">File: <code><%= configName %></code></div>
    </p>
    <form action="config" method="post">
        <textarea rows="20" cols="100" name="config"><%= configText %></textarea>
        <p>
            <input type="submit" value="submit" />
        </p>
    </form>

    <p>
        <a href=".">Back to other log files</a>
    </p>

</body>

</html>