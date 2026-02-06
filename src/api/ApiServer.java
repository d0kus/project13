package api;

import com.sun.net.httpserver.HttpServer;
import factory.RepositoryFactory;
import service.JoblistingService;
import service.PortalService;
import service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        var portalService = new PortalService(RepositoryFactory.portalRepository());
        var jobService = new JoblistingService(RepositoryFactory.joblistingRepository());
        var userService = new UserService();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Site
        server.createContext("/", new StaticFileHandler(java.nio.file.Path.of("web")));

        // API
        server.createContext("/api/ping", new PingHandler());
        server.createContext("/api/portals", new PortalHandler(portalService, jobService));
        server.createContext("/api/joblistings", new JoblistingHandler(jobService, portalService));
        server.createContext("/api/stats", new StatsHandler(portalService, jobService));
        server.createContext("/api/users", new UserHandler(userService));

        server.setExecutor(null);
        server.start();

        System.out.println("API started on http://localhost:" + port);
    }
}