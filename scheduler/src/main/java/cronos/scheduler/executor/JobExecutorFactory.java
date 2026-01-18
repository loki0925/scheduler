package cronos.scheduler.executor;

import cronos.scheduler.entity.Job;
import cronos.scheduler.entity.enums.JobType;
import cronos.scheduler.scheduler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobExecutorFactory {

    private final EmailJobExecutor emailJobExecutor;
    private final HttpJobExecutor httpJobExecutor;
    private final DummyJobExecutor dummyJobExecutor;
    private final ScriptJobExecutor scriptJobExecutor;
    private final FileSystemJobExecutor fileSystemJobExecutor;
    private final CacheJobExecutor cacheJobExecutor;
    private final DatabaseJobExecutor databaseJobExecutor;
    private final DbToKafkaJobExecutor dbToKafkaJobExecutor;
    private final ReportJobExecutor reportJobExecutor;


    public void execute(Job job) throws Exception {

        if (job.getJobType() == JobType.EMAIL) {
            emailJobExecutor.execute(job);

        } else if (job.getJobType() == JobType.HTTP) {
            httpJobExecutor.execute(job);


        }  else if (job.getJobType() == JobType.DUMMY) {
            dummyJobExecutor.execute(job);

        }
        else if (job.getJobType() == JobType.SCRIPT) {
            scriptJobExecutor.execute(job);
        }
        else if (job.getJobType() == JobType.FILE_SYSTEM) {
            fileSystemJobExecutor.execute(job);
        }
        else if (job.getJobType() == JobType.CACHE) {
            cacheJobExecutor.execute(job);
        }
        else if (job.getJobType() == JobType.DATABASE) {
            databaseJobExecutor.execute(job);
        }
        else if (job.getJobType() == JobType.DB_TO_KAFKA ) {
            dbToKafkaJobExecutor.execute(job);
        }
        else if (job.getJobType() == JobType.REPORT ) {
            reportJobExecutor.execute(job);
        }
        else {
            throw new IllegalArgumentException(
                    "Unsupported job type: " + job.getJobType()
            );
        }
    }
}
