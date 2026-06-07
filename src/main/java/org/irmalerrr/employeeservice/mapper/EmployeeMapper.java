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
public abstract class EmployeeMapper {

    public abstract EmployeeDto toDto(Employee employee);

    public abstract List<EmployeeDto> toDtoList(List<Employee> employees);

    public abstract EmployeeShortDto toShortDto(Employee employee);

    public abstract List<EmployeeShortDto> toShortDtoList(List<Employee> employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    @Mapping(target = "assignedTasks", ignore = true)
    @Mapping(target = "viewedTasks", ignore = true)
    public abstract Employee toEntity(CreateEmployeeDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    @Mapping(target = "assignedTasks", ignore = true)
    @Mapping(target = "viewedTasks", ignore = true)
    public abstract Employee updateEntity(@MappingTarget Employee entity, CreateEmployeeDto dto);
}
