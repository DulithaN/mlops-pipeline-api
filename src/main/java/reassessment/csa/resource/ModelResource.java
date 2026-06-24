package reassessment.csa.resource;

import reassessment.csa.exception.LinkedWorkspaceNotFoundException;
import reassessment.csa.model.MachineLearningModel;
import reassessment.csa.model.MLWorkspace;
import reassessment.csa.repository.MLOpsDataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/models")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {
    private final MLOpsDataStore store = MLOpsDataStore.getInstance();

    @GET
    public Response getModels(@QueryParam("status") String status) {
        List<MachineLearningModel> activeModels = new ArrayList<>(store.getModels().values());
        if (status != null && !status.trim().isEmpty()) {
            activeModels = activeModels.stream()
                    .filter(m -> m.getStatus().equalsIgnoreCase(status.trim()))
                    .collect(Collectors.toList());
        }
        return Response.ok(activeModels).build();
    }

    @POST
    public Response registerModel(MachineLearningModel model) {
        // Validation: Verify that the parent workspace target actively exists
        MLWorkspace parentWs = store.getWorkspaces().get(model.getWorkspaceId());
        if (parentWs == null) {
            throw new LinkedWorkspaceNotFoundException("Semantic Violation: Linked workspace context not found.");
        }

        // Standardized Architectural Requirement: Server-side secure UUID allocation
        String serverGeneratedId = "MOD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        model.setId(serverGeneratedId);
        model.setLatestAccuracy(0.0); // Reset initialization metrics
        
        // Persist objects inside safe zones
        store.getModels().put(serverGeneratedId, model);
        parentWs.getModelIds().add(serverGeneratedId);

        return Response.status(Response.Status.CREATED).entity(model).build();
    }

    // Sub-Resource Locator Pattern for Contextual Metric Logs Nested Navigation
    @Path("/{modelId}/metrics")
    public EvaluationMetricResource getEvaluationMetricSubResource(@PathParam("modelId") String modelId) {
        // Return resource instance context bound to sub-routing target
        return new EvaluationMetricResource(modelId);
    }
}