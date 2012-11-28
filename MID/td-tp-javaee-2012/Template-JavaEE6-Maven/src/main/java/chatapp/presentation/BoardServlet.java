package chatapp.presentation;

import chatapp.model.Participant;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/board")
public class BoardServlet extends HttpServlet {

  @Inject
  Participant incomingParticipant;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getServletContext().getRequestDispatcher("/WEB-INF/views/board.jsp").forward(request, response);
  }
/*
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    if (name != null && email != null) {
    
      incomingParticipant.setName(name);
      incomingParticipant.setEmail(email);

      response.sendRedirect("/thisiswar-1.0beta/board");
    }

    else {
      response.sendRedirect("/thisiswar-1.0beta/join");
    }
  }
  */
}
