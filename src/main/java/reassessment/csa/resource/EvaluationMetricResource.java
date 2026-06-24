package reassessment.csa.resource;

import reassessment.csa.exception.ModelDeprecatedException;
import reassessment.csa.model.EvaluationMetric;
import reassessment.csa.model.MachineLearningModel;
import reassessment.csa.repository.MLOpsDataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EvaluationMetricResource {
    private final String modelId;
    private final MLOpsDataStore store = MLOpsDataStore.getInstance();

    // Context transmitted from parent resource locator
    public EvaluationMetricResource(String modelId) {
        this.modelId = modelId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetricsHistory() {
        MachineLearningModel model = store.getModels().get(modelId);
        if (model == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<EvaluationMetric> evaluationLogs = store.getMetricsHistory().getOrDefault(modelId, new ArrayList<>());
        return Response.ok(evaluationLogs).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response appendMetric(EvaluationMetric metric) {
        MachineLearningModel model = store.getModels().get(modelId);
        if (model == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // State Constraint Validation
        if ("DEPRECATED".equalsIgnoreCase(model.getStatus())) {
            throw new ModelDeprecatedException("State Error: Cannot record fresh telemetry on a deprecated model.");
        }

        metric.setId(UUID.randomUUID().toString());
        metric.setTimestamp(System.currentTimeMillis());

        // Update historical records
        store.getMetricsHistory().computeIfAbsent(modelId, k -> new ArrayList<>()).add(metric);

        // Crucial Side-Effect Execution: Maintain direct entity status consistency
        model.setLatestAccuracy(metric.getAccuracyScore());

        return Response.status(Response.Status.CREATED).entity(metric).build();
    }
}