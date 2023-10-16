package com.vtv.inspection;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = { "cola1" })
    public void receive(@Payload OrderInspectionDto message) {

        log.info("Received JSON message {}", message.toString());

        makeSlow();
//TODO: Tener clase reparaciones con ESTADO PENDING o ordenes de reparaciones y reparaciones?
    }

    private void makeSlow() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}