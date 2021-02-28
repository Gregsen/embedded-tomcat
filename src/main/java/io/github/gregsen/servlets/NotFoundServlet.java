package io.github.gregsen.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class NotFoundServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(NotFoundServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.debug("Reqest: " + req.getQueryString());
        final PrintWriter writer = resp.getWriter();
        writer.write("{\"status\":\"404\",\"message\":\"not found\"}");
        writer.flush();
        writer.close();
    }
}
