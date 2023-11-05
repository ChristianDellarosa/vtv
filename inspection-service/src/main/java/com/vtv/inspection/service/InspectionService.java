package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;

public interface InspectionService {

    Inspection inspect(InspectionRequest inspectionRequest); //TODO: Ver si solo con el carPlate

    Inspection getByCarPlate(String carPlate); //TODO: Ver si solo con el carPlate
}
