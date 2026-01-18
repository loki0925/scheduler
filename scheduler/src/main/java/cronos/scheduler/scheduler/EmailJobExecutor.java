package cronos.scheduler.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import cronos.scheduler.dto.EmailPayload;
import cronos.scheduler.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobExecutor {

    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    public void execute(Job job) throws Exception {

        EmailPayload payload =
                objectMapper.readValue(job.getPayload(), EmailPayload.class);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@chronosapp.in");
        message.setTo(payload.getTo().toArray(new String[0]));
        message.setSubject(payload.getSubject());
        message.setText(payload.getBody());

        mailSender.send(message);
    }
}

