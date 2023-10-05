package com.vtv.appointment.service;


import com.vtv.appointment.model.dto.OrderInspectionDto;

public interface InspectionPublisherService {

   void send(OrderInspectionDto message);

   void orderInspection(OrderInspectionDto orderInspectionDto);
}
