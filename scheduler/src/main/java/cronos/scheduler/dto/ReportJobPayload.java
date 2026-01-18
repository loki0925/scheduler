package cronos.scheduler.dto;


import lombok.Data;

@Data
public class ReportJobPayload {

    private String sql;              // DB query
    private String reportName;       // sales_report
    private String format;           // CSV | PDF | XLS
    private String outputPath;       // /reports/
    private boolean emailRequired;   // true/false
    private String emailTo;          // optional
}

