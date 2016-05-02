package test.servlet;
 
import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
 
public class MyFilter implements Filter{
 
    public void destroy() {
        // TODO Auto-generated method stub
       
    }
 
    public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) srequest;
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(srequest, sresponse);
    }
 
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
       
    }
 
}