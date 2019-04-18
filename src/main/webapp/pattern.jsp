<%@ page import="com.exadel.exc.*, java.text.*, java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<%
String inName  = (String) request.getAttribute("inName"); // input file name
String pName   = (String) request.getAttribute("pName");  // value of "name" property that indicates input file
String pattern = (String) request.getAttribute("pattern");
String nameArg = pName!=null? "&amp;name="+pName:"";

LinkedList<Line> lines  = (LinkedList<Line>) request.getAttribute("lines");

/*
System.out.println("[pattern.jsp] inName="+inName);
System.out.println("[pattern.jsp] pName="+pName);
System.out.println("[pattern.jsp] pattern="+pattern);
System.out.println("[pattern.jsp] lines.size()="+lines.size());
*/
%>
<html>
<head>
    <title>Piece of Log</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="exc.css">
</head>
<body>

<h3>Matching lines in <span class="tt"><%= inName %></span></h3>
<p>
<div class="indent">Pattern: <code><%= pattern %></code></div>
<p>

<div class="pat tt">

<% int prev = -1; %>
<% for (Line line: lines) { %>
    <% if (line.lno-1>prev) { %><p><hr><p><% } %>
    <a class="lno nounder" href="block?<%= nameArg %>&lno=<%= line.lno %>"><%= line.lno %>:</a>
    <% if (line.match) { %> <span class="match"> <% } %>
    <%= line.text %><br>
    <% if (line.match) { %> </span> <% } %>
    <% prev = line.lno; %>
<% } %>

</div>

</body>
</html>
