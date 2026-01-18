package cronos.scheduler.entity.enums;

public enum JobType {

    ONE_TIME("One-time execution"),
    RECURRING("Recurring execution"),
    EMAIL("For email job"),
    HTTP("For http job"),
    SCRIPT("For script job"),
    DUMMY("For dummy job"),
    DATABASE("For database job"),
    FILE_SYSTEM("For file system job"),
    MESSAGE_QUEUE("Message queue"),

    CACHE("Cache job"),
    REPORT("Report job"),
    DB_TO_KAFKA("Database to Kafka"),


    BATCH("Batch processing");

    private final String description;

    JobType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
