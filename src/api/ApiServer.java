package api;

import com.sun.net.httpserver.HttpServer;
import repository.JoblistingRepository;
import repository.PortalRepository;
import service.JoblistingService;
import service.PortalService;
import factory.RepositoryFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        var portalService = new PortalService(RepositoryFactory.portalRepository());
        var jobService = new JoblistingService(RepositoryFactory.joblistingRepository());

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // было: server.createContext("/api/ping", exchange -> { ... });
        server.createContext("/api/ping", new PingHandler());

        server.createContext("/api/portals", new PortalHandler(portalService));
        server.createContext("/api/joblistings", new JoblistingHandler(jobService));

        server.setExecutor(null);
        server.start();

        System.out.println("API started on http://localhost:" + port);



        server.createContext("/", new StaticFileHandler(java.nio.file.Path.of("web")));


        server.createContext("/api/ping", new PingHandler());
        server.createContext("/api/portals", new PortalHandler(portalService));
        server.createContext("/api/joblistings", new JoblistingHandler(jobService));
        server.createContext("/api/stats", new StatsHandler(portalService, jobService));
    }
}