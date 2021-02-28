package io.github.gregsen.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerInfoServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ServerInfoServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.debug("Request: " + req.getQueryString());

        final String server_type = req.getParameter("server_type");

        if (server_type == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            final PrintWriter writer = resp.getWriter();
            writer.write("{\"status\":\"400\",\"message\":\"no server type provided\"}");
            writer.flush();
            writer.close();
            return;
        }

        if (server_type.equalsIgnoreCase("tomcat")) {
            final PrintWriter writer = resp.getWriter();
            writer.write("Tomcat Server!\n");
            writer.flush();
            writer.close();
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            final PrintWriter writer = resp.getWriter();
            writer.write("{\"status\":\"404\",\"message\":\"no server of type " + server_type + "\"}"); // not secure
            writer.flush();
            writer.close();
        }
    }
}
