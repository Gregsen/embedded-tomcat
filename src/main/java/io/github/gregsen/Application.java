package io.github.gregsen;


import io.github.gregsen.servlets.NotFoundServlet;
import io.github.gregsen.servlets.ServerInfoServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;

public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws LifecycleException {
        // We remove all handlers from the java.util.logger and then install the slf4j handler.
        // subsequent log calls to JUL will then be handled by slf4j.
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();


        LOG.info("I am logging!");
        LOG.debug("I am debugger");
        // Create a new tomcat server object
        final Tomcat tomcat = new Tomcat();
        // set the directory that will be used for temp files. This will be $CATALINA_HOME.
        // In a "traditional" Tomcat, this directory contains the /bin, /lib, etc folders.
        tomcat.setBaseDir(".");
        // setting the port. If left out, it defaults to 8080
        tomcat.setPort(8080);


        /*
         * Add a org.apache.catalina.Context. In a nutshell, a Context is a web application. The contextPath is the URL after the
         * server name (i.e. "api" in myserver.com/api. If left as an empty string, it will be the root url (i.e. myserver.com/).
         * The pathname is the folder that contains the static files (e.g. css, images, etc. if they exist).
         * If the folder does not exist, the application will not start. It can contain relative paths (i.e. ".", "..").
         */
        final Context context = tomcat.addContext("", new File(".").getAbsolutePath());

        /*
         * Enable an HTTPConnector (http 1.1). Prior to v9, Tomcat did this by default, now it has to be done by hand.
         * It is also possible to create a <code>new Connector()</code> and supply this via the <code>tomcat.setConnector()</code>.
         * This way, you could configure the connector with custom values for number of threads, timeouts, etc.
         */
        tomcat.getConnector();


        // Adding new servlets can be done by using a static method and providing the context, in which the servlet should run,
        // a name for the servlet and the actual class. The class has to implement jakarta.servlet.Servlet.
        Tomcat.addServlet(context, "serverInfo", new ServerInfoServlet());
        Tomcat.addServlet(context, "notFound", new NotFoundServlet());

        /*
         * Adding a mapping from an URL to a servlet that will handle the HTTP request coming via this URL.
         * If the servlet does not have an HTTP method for the request (i.e. GET, POST, etc), a 405 error will be thrown
         */
        context.addServletMappingDecoded("/*", "notFound"); // catchall to prevent default tomcat error page
        context.addServletMappingDecoded("/api/*", "serverInfo");

        // Start the server. This will initialize all the servlets at once (so, no need to call tomcat.init() ).
        tomcat.start();
        LOG.info("------");
        LOG.info("doc base - " + context.getDocBase());
        LOG.info("basename - " + context.getBaseName());
        LOG.info("catalina home - " + context.getCatalinaHome().getAbsolutePath());
        //The server runs and handles requests until it receives a shutdown signal. Every request will be checked against
        // the list of servlets and either be send to the servlet class or the server returns a 404
        tomcat.getServer().await();
    }
}
