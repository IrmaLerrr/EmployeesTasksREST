package org.irmalerrr.employeeservice.mapper;

import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeShortDto;
import org.irmalerrr.employeeservice.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
//todo - DONE - Попробуй в качестве маппера mapstruct. он очень попсовый и удобный, используется наверное везде)
//todo - DONE - у тебя тут  маппер который трудится на двух работах(работает с Тасками и имплоями). лучше разделить ответственность на 2 маппера, пусть каждый занимается своими классами)
//todo - DONE -  вынеси мапперы из сервисов в папку mapper
@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    public abstract EmployeeDto toDto(Employee employee);

    public abstract EmployeeShortDto toShortDto(Employee employee);

    public abstract List<EmployeeShortDto> toShortDtoList(List<Employee> employee);

    public abstract List<EmployeeDto> toDtoList(List<Employee> employees);

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
