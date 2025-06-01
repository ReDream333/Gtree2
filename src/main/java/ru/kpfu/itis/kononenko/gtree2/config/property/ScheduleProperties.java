package ru.kpfu.itis.kononenko.gtree2.config.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "schedule")
public class ScheduleProperties {

//    private Map<JobType, JobSchedule> jobs;

    @Getter
    @Setter
    public static class JobSchedule {
        private boolean enabled;
        private String cron;
    }

}
