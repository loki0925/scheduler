package cronos.scheduler.dto;


import lombok.Data;

import java.util.List;

@Data
public class ScriptJobPayload {

    private String interpreter;   // bash | python | node
    private String scriptPath;    // absolute or allowed directory
    private List<String> args;    // optional arguments
    private int timeoutSeconds;   // execution timeout
}

