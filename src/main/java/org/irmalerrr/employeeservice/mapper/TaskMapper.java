package org.irmalerrr.employeeservice.mapper;


import org.irmalerrr.employeeservice.dto.*;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.model.Employee;
import org.irmalerrr.employeeservice.model.Task;
import org.irmalerrr.employeeservice.repository.EmployeeRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
public abstract class TaskMapper {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "viewers", source = "viewers")
    public abstract TaskDto toDto(Task task);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "viewers", source = "viewers")
    public abstract List<TaskDto> toDtoList(List<Task> task);

    @Mapping(target = "author", expression = "java(getEmployeeFromDto(dto.getAuthor()))")
    @Mapping(target = "assignee", expression = "java(getEmployeeFromDto(dto.getAssignee()))")
    @Mapping(target = "viewers", expression = "java(getEmployeesFromDto(dto.getViewers()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Task toEntity(CreateTaskDto dto);

    @Mapping(target = "author", expression = "java(getEmployeeFromDto(dto.getAuthor()))")
    @Mapping(target = "assignee", expression = "java(getEmployeeFromDto(dto.getAssignee()))")
    @Mapping(target = "viewers", expression = "java(getEmployeesFromDto(dto.getViewers()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Task updateEntity(@MappingTarget Task entity, CreateTaskDto dto);

    protected Employee getEmployeeFromDto(Long id) {
        if (id == null) return null;
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    protected List<Employee> getEmployeesFromDto(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        return ids.stream()
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new EmployeeNotFoundException(id)))
                .collect(Collectors.toList());
    }
}
