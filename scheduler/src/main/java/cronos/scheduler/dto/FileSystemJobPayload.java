package cronos.scheduler.dto;


import lombok.Data;

@Data
public class FileSystemJobPayload {

    private String operation;      // MOVE, COPY, DELETE, RENAME, CREATE_DIR
    private String sourcePath;     // file or directory
    private String targetPath;     // optional (MOVE, COPY, RENAME)
    private boolean overwrite;     // overwrite existing file
}

