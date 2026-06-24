package reassessment.csa.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response getDiscoveryInfo() {
        Map<String, Object> discovery = new LinkedHashMap<>();
        discovery.put("system", "Westminster AI Lab Cloud Infrastructure");
        discovery.put("apiVersion", "1.0.0-Reassessment");
        discovery.put("adminContact", "architect.support@westminster.ac.uk");
        
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("workspaces", "/api/v1/workspaces");
        endpoints.put("models", "/api/v1/models");
        discovery.put("collections", endpoints);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(86400); // 24-hour retention for static metadata
        cc.setPrivate(false);

        return Response.ok(discovery).cacheControl(cc).build();
    }
}