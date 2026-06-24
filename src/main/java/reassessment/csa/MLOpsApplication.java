package reassessment.csa;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationPath("/api/v1")
public class MLOpsApplication extends Application {
    private static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        // Automatically scan project packages for resources, providers, and filters
        final ResourceConfig rc = new ResourceConfig().packages("reassessment.csa");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        try {
            final HttpServer server = startServer();
            Logger.getLogger(MLOpsApplication.class.getName()).info(
                String.format("MLOps Pipeline Management API successfully deployed at %sapi/v1\nHit CTRL+C to terminate.", BASE_URI)
            );
            Thread.currentThread().join();
        } catch (Exception e) {
            Logger.getLogger(MLOpsApplication.class.getName()).log(Level.SEVERE, "Server boot sequence failed", e);
        }
    }
}