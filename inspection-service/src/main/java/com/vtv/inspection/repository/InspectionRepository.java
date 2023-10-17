package com.vtv.inspection.repository;

import com.vtv.inspection.model.domain.Inspection;

import java.util.List;

public interface InspectionRepository {
    Inspection save(Inspection inspection);

    List<Inspection> getByCarPlate(String carPlate);
}
