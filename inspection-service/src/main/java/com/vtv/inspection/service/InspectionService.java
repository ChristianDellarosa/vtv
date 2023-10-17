package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.domain.InspectionResponse;

public interface InspectionService {

    InspectionResponse inspect(InspectionRequest inspectionRequest); //TODO: Ver si solo con el carPlate

    InspectionResponse getByCarPlate(String carPlate); //TODO: Ver si solo con el carPlate
}
