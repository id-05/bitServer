package ru.bitServer.util;

import ru.bitServer.dao.UserDao;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter, UserDao {

    public AuthorizationFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException {
        HttpServletRequest sRequest = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            HttpSession ses = sRequest.getSession(false);
            String reqURI = sRequest.getRequestURI();
            if (reqURI.contains("/views/login.xhtml") || (ses != null && ses.getAttribute("userid") != null)
                    || reqURI.contains("/public/")
                    || reqURI.contains("/ariadna/")
                    || reqURI.contains("/errorpage")
                    || reqURI.contains("javax.faces.resource"))
                chain.doFilter(request, response);
            else
                resp.sendRedirect(sRequest.getContextPath() + "/views/login.xhtml");
        } catch (Exception e) {

            LogTool.getLogger().error("Error in AuthorizationFilter: " + e.getMessage());
            resp.sendRedirect(sRequest.getContextPath() + "/views/errorpage.xhtml");
        }
    }

    @Override
    public void destroy() {

    }
}
