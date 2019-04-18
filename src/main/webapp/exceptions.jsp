<%@ page import="com.exadel.exc.*, java.util.*, java.text.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%
String inName  = (String) request.getAttribute("inName"); // input file name
String pName   = (String) request.getAttribute("pName");  // value of "name" property that indicates input file
String nameArg = pName!=null? "name="+pName:"";
Integer totalCount = Utils.getTotalCount(inName, pName, request.getSession());

LinkedList<Exc> exceptions = (LinkedList<Exc>) request.getAttribute("exceptions"); // list of exceptions
%>
<html>
<head>
  <title>Log Exceptions</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" href="exc.css">
</head>
<body>

<h3>Exceptions from <code><%= inName %></code></h3>
<p>
<table class="exc-table">

<tr>
  <th> Num </th>
  <th> LineNum </th>
  <th> Time </th>
  <th> Exception </th>
  <th> Comment </th>
</tr>

<% int k = exceptions.size();
%>
<% for (Exc exc: exceptions) { %>
<tr>
  <td>№<%= k-- %></td>
  <td class="lno">
    <a href="block?<%= nameArg %>&lno=<%= exc.lno %>"><%= exc.lno %></a>
  </td>
  <td class="time">
    <%= exc.time==null?"":exc.time %>
  </td>
  <td ><code><%= exc.sig %></code></td>
  <td class="cmt">
    <code> <%= exc.cmt==null?"":exc.cmt %> </code>
  </td>
</tr>
<% } %>

<tr>
  <th colspan="5"> Lines in log file: <a href="block?<%= pName!=null? "name="+pName+"&":"" %>lno=<%= totalCount %>"><%= totalCount %></a> </th>
</tr>

</table>

<p>
  <form action="block" method="post">
    Find time: <input type="text" name="tstamp" size="25">
    <input type="hidden" value="<%= inName %>">
    <input type="submit" value="Submit">
  </form>
<p>

<p>
<a href=".">Back to other log files</a>
<p>

</body>
</html>
