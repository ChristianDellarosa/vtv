package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;

import java.util.List;

public interface InspectionService {

    Inspection inspect(String authToken, InspectionRequest inspectionRequest); //TODO: Ver si solo con el carPlate

    List<Inspection> getByCarPlate(String carPlate); //TODO: Ver si solo con el carPlate
}
