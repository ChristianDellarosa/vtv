package com.vtv.appointment.client;

public interface ProducerClient<T> {
    void send(T message);
}
