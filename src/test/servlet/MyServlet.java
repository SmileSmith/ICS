package test.servlet;
 
import java.io.IOException;
import java.util.Enumeration;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
 
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("calling MyServlet");
        Enumeration e=request.getParameterNames();
        while(e.hasMoreElements()){
            String name = (String)e.nextElement();
            String value=request.getParameter(name);
            System.out.println("name="+name+":value="+value);
        }      
    }
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
