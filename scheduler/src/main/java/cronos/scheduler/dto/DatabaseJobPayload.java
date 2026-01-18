package cronos.scheduler.dto;


import lombok.Data;

@Data
public class DatabaseJobPayload {

    private String operation;     // SELECT, UPDATE, DELETE, INSERT, PROCEDURE, SCRIPT
    private String dataSource;    // PRIMARY (future: reporting, replica)
    private String sql;           // query / update / procedure call
    private boolean transactional;
    private int timeoutSeconds;
}

