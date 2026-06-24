package reassessment.csa.repository;

import reassessment.csa.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MLOpsDataStore {
    private static final MLOpsDataStore INSTANCE = new MLOpsDataStore();

    private final Map<String, MLWorkspace> workspaces = new ConcurrentHashMap<>();
    private final Map<String, MachineLearningModel> models = new ConcurrentHashMap<>();
    private final Map<String, List<EvaluationMetric>> metricsHistory = new ConcurrentHashMap<>();

    private MLOpsDataStore() {
        // Seed initial data to enable out-of-the-box functional verification
        MLWorkspace seedWs = new MLWorkspace("WS-VISION-01", "Computer Vision Lab", 500);
        workspaces.put(seedWs.getId(), seedWs);
    }

    public static MLOpsDataStore getInstance() { return INSTANCE; }

    public Map<String, MLWorkspace> getWorkspaces() { return workspaces; }
    public Map<String, MachineLearningModel> getModels() { return models; }
    public Map<String, List<EvaluationMetric>> getMetricsHistory() { return metricsHistory; }
}