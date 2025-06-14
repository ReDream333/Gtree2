package ru.kpfu.itis.kononenko.gtree2.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.entity.TreeSubscription;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.TreeSubscriptionRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.UserRepository;
import ru.kpfu.itis.kononenko.gtree2.service.impl.MailService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BirthdayReminderJob {

    private final NodeRepository nodeRepository;
    private final TreeSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Scheduled(cron = "0 9 7 * * *")
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        int month = tomorrow.getMonthValue();
        int day = tomorrow.getDayOfMonth();
        List<Node> nodes = nodeRepository.findByBirthday(month, day);
        if (nodes.isEmpty()) return;

        Map<Long, List<Node>> nodesByTree = nodes.stream()
                .collect(Collectors.groupingBy(n -> n.getTree().getId()));

        for (Map.Entry<Long, List<Node>> entry : nodesByTree.entrySet()) {
            Long treeId = entry.getKey();
            List<TreeSubscription> subs = subscriptionRepository.findByTreeId(treeId);
            if (subs.isEmpty()) continue;

            String message = entry.getValue().stream()
                    .map(n -> (n.getFirstName() != null ? n.getFirstName() : "") +
                            (n.getLastName() != null ? " " + n.getLastName() : ""))
                    .collect(Collectors.joining("\n"));
            String subject = "Напоминание о дне рождения";
            String text = "Завтра день рождения:\n" + message;

            for (TreeSubscription sub : subs) {
                User user = userRepository.findById(sub.getUserId()).orElse(null);
                if (user != null && user.getEmail() != null) {
                    mailService.sendSimpleEmail(user.getEmail(), subject, text);
                }
            }
        }
    }
}