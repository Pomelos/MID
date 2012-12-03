package chatapp.presentation;

import chatapp.model.Participant;
import chatapp.model.ChatMessage;
import chatapp.components.ChatBoard;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.ejb.*;
import java.util.Date;

@WebServlet(urlPatterns = "/board")
public class BoardServlet extends HttpServlet {

  @Inject
  Participant incomingParticipant;
  
  @EJB
  ChatBoard myChatBoard;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setAttribute("messages", myChatBoard.getList());
    request.getServletContext().getRequestDispatcher("/WEB-INF/views/board.jsp").forward(request, response);
  }
  

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  Date today = new Date();
	  String message = request.getParameter("message");
	  ChatMessage chatmessage = new ChatMessage(incomingParticipant, today, message);     
      myChatBoard.addMessage(chatmessage);

      response.sendRedirect("/thisiswar-1.0beta/board");

  }
}
