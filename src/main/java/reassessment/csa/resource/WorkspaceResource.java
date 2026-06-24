package reassessment.csa.resource;

import reassessment.csa.exception.WorkspaceNotEmptyException;
import reassessment.csa.model.MLWorkspace;
import reassessment.csa.repository.MLOpsDataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;

@Path("/workspaces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkspaceResource {
    private final MLOpsDataStore store = MLOpsDataStore.getInstance();

    @GET
    public Response getAllWorkspaces() {
        Collection<MLWorkspace> workspaceList = store.getWorkspaces().values();
        CacheControl cc = new CacheControl();
        cc.setMaxAge(60); // Cache allowed for 60 seconds to relieve engine pressure
        return Response.ok(new ArrayList<>(workspaceList)).cacheControl(cc).build();
    }

    @POST
    public Response createWorkspace(MLWorkspace workspace) {
        if (workspace.getId() == null || workspace.getId().trim().isEmpty()) {
            throw new WebApplicationException("Workspace identifier required", Response.Status.BAD_REQUEST);
        }
        store.getWorkspaces().put(workspace.getId(), workspace);
        return Response.status(Response.Status.CREATED).entity(workspace).build();
    }

    @GET
    @Path("/{workspaceId}")
    public Response getWorkspaceById(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace ws = store.getWorkspaces().get(workspaceId);
        if (ws == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ws).build();
    }

    @HEAD
    @Path("/{workspaceId}")
    public Response checkWorkspaceExistence(@PathParam("workspaceId") String workspaceId) {
        if (!store.getWorkspaces().containsKey(workspaceId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().header("X-Resource-Exists", "true").build();
    }

    @DELETE
    @Path("/{workspaceId}")
    public Response deleteWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace ws = store.getWorkspaces().get(workspaceId);
        if (ws == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Business Logic Constraint: Prevent data orphans
        if (!ws.getModelIds().isEmpty()) {
            throw new WorkspaceNotEmptyException("Execution Blocked: Target workspace has active model deployments.");
        }
        store.getWorkspaces().remove(workspaceId);
        return Response.noContent().build();
    }
}