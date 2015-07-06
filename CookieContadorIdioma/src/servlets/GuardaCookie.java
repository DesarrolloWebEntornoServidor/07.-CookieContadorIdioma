package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GuardaCookie", urlPatterns = {"/GuardaCookie"})
public class GuardaCookie extends HttpServlet {

    protected void procesaSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Creación de la cookie para el cliente
            Cookie unaCookie;
            String nombreCookie=request.getParameter("usuario");
            String contenidoCookie=request.getParameter("ciclo")+","+request.getParameter("contVisitas");
            unaCookie=new Cookie(nombreCookie, contenidoCookie);
            response.addCookie(unaCookie);
            
            // Redirección al formularioContador.html
            RequestDispatcher rd=request.getRequestDispatcher("formularioContador.html");
            rd.forward(request, response);
        }
        catch(Exception e) {
            
        }
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesaSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesaSolicitud(request, response);
    }
}