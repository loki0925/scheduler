package cronos.scheduler.service;


import cronos.scheduler.service.inter.DatabaseAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JdbcDatabaseAccessService implements DatabaseAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> executeSelect(String sql, int timeoutSeconds) {
        jdbcTemplate.setQueryTimeout(timeoutSeconds);
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public int executeUpdate(String sql, int timeoutSeconds) {
        jdbcTemplate.setQueryTimeout(timeoutSeconds);
        return jdbcTemplate.update(sql);
    }

    @Override
    public void executeProcedure(String sql, int timeoutSeconds) {
        jdbcTemplate.setQueryTimeout(timeoutSeconds);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void executeScript(String sql, int timeoutSeconds) {
        jdbcTemplate.setQueryTimeout(timeoutSeconds);
        jdbcTemplate.execute(sql);
    }
    public List<Map<String, Object>> fetchBatch(
            String sql,
            int batchSize,
            Object lastProcessedValue
    ) {
        return jdbcTemplate.queryForList(
                sql + " WHERE id > ? ORDER BY id ASC LIMIT ?",
                lastProcessedValue, batchSize
        );
    }

}
