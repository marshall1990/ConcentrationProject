<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.File" session="false" %>
<%@ page import="java.io.File" session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<title>Badanie koncentracji</title>
<link rel="stylesheet" href="css/style.css" type="text/css" />
<link rel="stylesheet" href="css/colors.css" type="text/css" />
<link rel="stylesheet" href="css/backgrounds.css" type="text/css" />
<link rel="stylesheet" media="(max-width: 640px)" href="css/mobile.css" type="text/css" />
</head>
<body>

<div class="papers">
<h1>Test koncentracji</h1>
<form class="home" action="MainServlet" method="post">
<label> Podaj swoje imię:
<input type="text" name="username"/>
</label>
<p>Wybierz test, który chcesz wykonać:</p>
<%
File[] files = new File(getServletContext().getRealPath("\\scenarios\\")).listFiles();

for (int i = 0; i < files.length; i++) {
	if (files[i].getName().equals("scenario"+(i+1)+".xml"))%>
		<button name="test" value="<%=(i)%>" type="submit" class="button">Test <%=(i+1)%></button>
<%}%>

</form>
<h6 class="footer">Politechnika Krakowska - Wydział Inżynierii Elektrycznej i Komputerowej :: 2014.</h6>
</div>
</body>
</html>