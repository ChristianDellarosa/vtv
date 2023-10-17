package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.InspectionResult;

public interface InspectionService {

    InspectionResult inspect(String carPlate); //TODO: Ver si solo con el carPlate
}
