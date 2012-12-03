<%@ page import="java.util.LinkedList" %>
<%@ page import="chatapp.model.ChatMessage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="refresh" content="5" />
    <title>Welcome to FFEUFFCHAT !</title>
  </head>
  <body>
    
<% for (ChatMessage message : (LinkedList<ChatMessage>) request.getAttribute("messages")) { 
  
out.println(message.getMessage());

 } %>
  <br/><br/><br/>
  
  <form method='POST'>
	<p>
        <input type="text" name="message"/>
                
        <br/> <br/>
        
        <input type="submit" value="FFEUFF IT !"/>
    </p>
  </form>
        

  
  </body>
</html>
