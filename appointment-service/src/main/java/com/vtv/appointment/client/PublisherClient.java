package com.vtv.appointment.client;

public interface PublisherClient<T> {
    void send(T message);
}
