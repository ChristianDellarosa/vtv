package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.domain.InspectionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InspectionServiceImpl implements InspectionService {
    @Override
    public InspectionResponse inspect(InspectionRequest inspection) {
        return null;
    }

    @Override
    public InspectionResponse getByCarPlate(String carPlate) {
        return null;
    }
}
