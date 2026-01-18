package cronos.scheduler.service.inter;


import java.util.List;
import java.util.Map;

public interface DatabaseAccessService {

    List<Map<String, Object>> executeSelect(String sql, int timeoutSeconds);

    int executeUpdate(String sql, int timeoutSeconds);

    void executeProcedure(String sql, int timeoutSeconds);

    void executeScript(String sql, int timeoutSeconds);

    public List<Map<String, Object>> fetchBatch(String sql,int batchSize, Object lastProcessedValue);
}

