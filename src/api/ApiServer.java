package api;

import com.sun.net.httpserver.HttpServer;
import repository.JoblistingRepository;
import repository.PortalRepository;
import service.JoblistingService;
import service.PortalService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        var portalService = new PortalService(new PortalRepository());
        var jobService = new JoblistingService(new JoblistingRepository());

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // было: server.createContext("/api/ping", exchange -> { ... });
        server.createContext("/api/ping", new PingHandler());

        server.createContext("/api/portals", new PortalHandler(portalService));
        server.createContext("/api/joblistings", new JoblistingHandler(jobService));

        server.setExecutor(null);
        server.start();

        System.out.println("API started on http://localhost:" + port);
    }
}