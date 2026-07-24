package org.irmalerrr.employeeservice.mapper;

import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeShortDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto toDto(Employee employee);

    List<EmployeeDto> toDtoList(List<Employee> employees);

    EmployeeShortDto toShortDto(Employee employee);

    List<EmployeeShortDto> toShortDtoList(List<Employee> employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    @Mapping(target = "assignedTasks", ignore = true)
    @Mapping(target = "viewedTasks", ignore = true)
    Employee toEntity(CreateEmployeeDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    @Mapping(target = "assignedTasks", ignore = true)
    @Mapping(target = "viewedTasks", ignore = true)
    Employee updateEntity(@MappingTarget Employee entity, CreateEmployeeDto dto);
}
