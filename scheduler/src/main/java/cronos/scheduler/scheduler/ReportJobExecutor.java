package cronos.scheduler.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import cronos.scheduler.dto.ReportJobPayload;
import cronos.scheduler.entity.Job;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportJobExecutor {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final EmailJobExecutor emailJobExecutor;

    public void execute(Job job) throws Exception {

        ReportJobPayload payload =
                objectMapper.readValue(job.getPayload(), ReportJobPayload.class);

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(payload.getSql());

        String fileName = payload.getReportName() + "_" +
                LocalDateTime.now() + ".csv";

        Path filePath = Path.of(payload.getOutputPath(), fileName);

        generateCsv(rows, filePath.toString());

        /*if (payload.isEmailRequired()) {
            emailJobExecutor.sendAttachment(
                    payload.getEmailTo(),
                    "Report Generated",
                    "Please find attached report",
                    filePath.toString()
            );
        }*/
    }

    private void generateCsv(List<Map<String, Object>> rows, String filePath)
            throws Exception {

        try (FileWriter writer = new FileWriter(filePath)) {

            if (rows.isEmpty()) return;

            // Header
            writer.append(String.join(",", rows.get(0).keySet()));
            writer.append("\n");

            // Rows
            for (Map<String, Object> row : rows) {
                writer.append(
                        row.values().stream()
                                .map(v -> v == null ? "" : v.toString())
                                .reduce((a, b) -> a + "," + b)
                                .orElse("")
                );
                writer.append("\n");
            }
        }
    }
}

