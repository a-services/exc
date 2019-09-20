<%@ page import="com.exadel.exc.*, java.util.*, java.text.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%
List<String> logFiles  = (List<String>) request.getAttribute("logFiles"); // list of log file codes
Map<String,String> logNames = (Map<String,String>) request.getAttribute("logNames"); // mapping log file code to description
System.out.println("--- Path: " + (new java.io.File("").getCanonicalPath()));
%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <style>
    <%@include file="exc.css"%>
  </style>
  <title>exc</title>

</head>

<body>
  <h3>exc.war: web utility to check logs</h3>

  <table class="exc-table">

    <tr>
      <th>
        Env log
      </th>
      <th>
        Exceptions
      </th>
    </tr>

    <%
  boolean bluetag = true;
  for (String code: logFiles) {
%>
    <tr <%= bluetag? "class=bluetag":"" %>>
      <td>
        <%= logNames.get(code) %>
      </td>
      <td><a href="/exc/exc?name=<%= code %>">
          /exc/exc?name=<%= code %>
        </a></td>
    </tr>
    <%
  bluetag = !bluetag;
  }
%>

  </table>

  <p>Exception can be clicked to see appropriate part of log.<br />
    Current line will be highlighted.<br />First or last line of log in browser can be clicked to scroll the log.</p>

  <p><small>Version 20/Sep/2019, see on <a href="https://github.com/a-services/exc">GitHub</a></small></p>
</body>

</html>