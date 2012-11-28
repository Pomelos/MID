<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Welcome to FFEUFFCHAT !</title>
  </head>
  <body>
  
  Welcome to FFEUFFChat. Please provide us with a username and your valid email address.
  
  <br/> <br/>
 
  
  <form method="POST">
    <p>
        <label for="item">name :</label>
        <input id="item" type="text" name="name"/>
        <input type="hidden" name="action" value="name"/>
        <br/> <br/>
        
        <label for="item">email :</label>
        <input id="item" type="text" name="email"/>
        <input type="hidden" name="action" value="email"/>
        <br/> <br/>
        
        <input type="submit" value="Launch FFEUFFCHAT!"/>
        
    </p>
</form>
  </body>
</html>
