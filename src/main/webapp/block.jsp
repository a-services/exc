<%--
  :tabSize=2:indentSize=2:noTabs=true:
  :folding=explicit:collapseFolds=1:
  --%>
<%@ page import="com.exadel.exc.*, java.text.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<%
  String inName  = (String) request.getAttribute("inName"); // input file name
  String pName   = (String) request.getAttribute("pName");  // value of "name" property that indicates input file
  String nameArg = pName!=null? "name="+pName:"";

  String[] lines  = (String[]) request.getAttribute("lines");
  Integer n1  = (Integer) request.getAttribute("n1");
  Integer n2  = (Integer) request.getAttribute("n2");
  Integer lno  = (Integer) request.getAttribute("lno");
  Integer totalCount = Utils.getTotalCount(inName, pName, request.getSession());

  //System.out.println("[block.jsp] n1="+n1+", n2="+n2+", totalCount="+totalCount);
%>
<html>
<head>
  <title>Log <%= n1 %>-<%= n2 %></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style><%@include file="exc.css"%></style>
</head>
<body>

<%-- Page header --%>
<h3>Excerpt from <span class="tt"><%= inName %></span></h3>
<p>
<a href="exc<%= pName!=null? "?name="+pName:"" %>">Back to list of exceptions</a>
<p>

<%-- Piece of log --%>
<div class="block tt">
<% if (n1==1) { %><hr/><% } %>

<%-- {{{ Go through lines specified as block boundaries --%>
<% for (int k=n1; k<=n2; k++) { %>
  <a class="num nounder" href="block?<%= nameArg %>&lno=<%= k %>"><%= k %>:</a>
  <%
   // Assign `cur` style to current line
   if (k==lno) {
     out.print("<span class=\"cur\">");
   }

   /*
   // System.out.println("[block.jsp] #"+(k-n1)+": "+lines[k-n1]);
   boolean markLine = lines[k-n1].contains(".mark.");
   if (markLine) {
     out.print("<span style=\"mark\">");
   }
   */

   out.print(lines[k-n1]+"<br>");

   /*
   if (olamLine) {
     out.print("</span>");
   }
   */

   // Closw `cur` style for current line
   if (k==lno) {
     out.print("</span>");
   }

  %>
<% } %>
<%-- }}} --%>

<%
  //System.out.println("[block.jsp] n2="+n2+", totalCount="+totalCount+", "+(n2-totalCount));
  if (n2.equals(totalCount)) {
	 out.println("<hr/>");
   }
 %>
</div>

</body>
</html>
