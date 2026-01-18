package cronos.scheduler.scheduler;


import cronos.scheduler.dto.FileSystemJobPayload;
import cronos.scheduler.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.*;

@Component
@RequiredArgsConstructor
public class FileSystemJobExecutor {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public void execute(Job job) throws Exception {

        FileSystemJobPayload payload =
                objectMapper.readValue(job.getPayload(), FileSystemJobPayload.class);

        Path source = payload.getSourcePath() != null
                ? Paths.get(payload.getSourcePath())
                : null;

        Path target = payload.getTargetPath() != null
                ? Paths.get(payload.getTargetPath())
                : null;

        switch (payload.getOperation()) {

            case "MOVE" -> move(source, target, payload.isOverwrite());
            case "COPY" -> copy(source, target, payload.isOverwrite());
            case "DELETE" -> delete(source);
            case "RENAME" -> rename(source, target, payload.isOverwrite());
            case "CREATE_DIR" -> createDir(source);
            default -> throw new IllegalArgumentException("Unsupported FS operation");
        }
    }

    private void move(Path src, Path dest, boolean overwrite) throws Exception {
        Files.move(
                src,
                dest,
                overwrite ? StandardCopyOption.REPLACE_EXISTING : StandardCopyOption.ATOMIC_MOVE
        );
    }

    private void copy(Path src, Path dest, boolean overwrite) throws Exception {
        Files.copy(
                src,
                dest,
                overwrite ? StandardCopyOption.REPLACE_EXISTING : StandardCopyOption.COPY_ATTRIBUTES
        );
    }

    private void delete(Path src) throws Exception {
        Files.deleteIfExists(src);
    }

    private void rename(Path src, Path dest, boolean overwrite) throws Exception {
        move(src, dest, overwrite);
    }

    private void createDir(Path path) throws Exception {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}

