package cronos.scheduler.scheduler;



import cronos.scheduler.dto.ScriptJobPayload;
import cronos.scheduler.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ScriptJobExecutor {

    private static final Set<String> ALLOWED_INTERPRETERS =
            Set.of("bash", "python", "node");

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public void execute(Job job) throws Exception {

        ScriptJobPayload payload =
                objectMapper.readValue(job.getPayload(), ScriptJobPayload.class);

        validate(payload);

        List<String> command = new ArrayList<>();
        command.add(payload.getInterpreter());
        command.add(payload.getScriptPath());
        if (payload.getArgs() != null) {
            command.addAll(payload.getArgs());
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        Instant start = Instant.now();

        boolean finished =
                process.waitFor(payload.getTimeoutSeconds(),
                        java.util.concurrent.TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("Script execution timed out");
        }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        if (process.exitValue() != 0) {
            throw new RuntimeException("Script failed:\n" + output);
        }

        System.out.println("🟢 SCRIPT JOB OUTPUT:\n" + output);
    }

    private void validate(ScriptJobPayload payload) {
        if (!ALLOWED_INTERPRETERS.contains(payload.getInterpreter())) {
            throw new IllegalArgumentException("Interpreter not allowed");
        }
        if (payload.getTimeoutSeconds() <= 0) {
            payload.setTimeoutSeconds(60);
        }
    }
}

