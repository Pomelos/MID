<%@ page import="java.util.LinkedList" %> 
<%@ page import="chatapp.model.ChatMessage" %>
 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="refresh" content="5" />
    <title>ChatApp</title>
</head>
<body>

<% for (ChatMessage message : (LinkedList<ChatMessage>) request.getAttribute("messages")) { 
  
out.println(" From : " + message.getTransmitter().getName()+ " ("+message.getDate().toString()+") : " + message.getMessage()+"<br/>");	
out.println("---");

 }%>

<form method="POST">
    <p>
         <input type="text" name="message"/>
		 <input type="submit" value="send"/>
    </p>
</form>
	
</body>
</html>
