package org.demo.toxiproxy.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.toxiproxy.service.KafkaProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerScheduler {

    private final KafkaProducerService kafkaProducerService;

    @Scheduled(initialDelayString = "${scheduled.initial-delay}", fixedDelayString = "${scheduled.fixed-delay}")
    public void produceOrderEventMessages() {
        log.info("Starting to send Order Events to kafka");
        kafkaProducerService.sendOrderEventMessages();
        log.info("Sending Order Events to kafka ended");
    }
}
