package com.vtv.inspection.repository;

import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;

import java.util.List;

public interface InspectionRepository {
    Inspection save(Inspection inspection); //save and update

    List<Inspection> getByCarPlateAndAppointmentType(String carPlate, AppointmentType appointmentType);
}
