package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    void updateMedicine(@MappingTarget Medicine medicine, Medicine updateMedicine);
}
