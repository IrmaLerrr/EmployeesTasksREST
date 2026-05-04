package org.irmalerrr.employeeservice.mapper;


import lombok.RequiredArgsConstructor;
import org.irmalerrr.employeeservice.dto.*;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.repository.EmployeeRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
@RequiredArgsConstructor
public abstract class TaskMapper {
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
                .toList();
    }
}
