package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.entity.MedicineDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    @Mapping(target = "id", ignore = true)
    void updateMedicine(@MappingTarget MedicineDetail medicineDetail, MedicineDetail updateMedicineDetail);
}
