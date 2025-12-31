package cronos.scheduler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator implements CommandLineRunner {

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        System.out.println(encoder.encode("password"));
    }
}
