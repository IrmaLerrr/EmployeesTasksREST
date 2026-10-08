package org.irmalerrr.employeeservice.mapper;


import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
public interface TaskMapper {

    TaskDto toDto(Task task);

    List<TaskDto> toDtoList(List<Task> task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "viewers", source = "viewers")
    Task toEntity(CreateTaskDto dto, Employee author, Employee assignee, List<Employee> viewers);

    @Mapping(target = "viewers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    Task updateEntity(@MappingTarget Task entity, CreateTaskDto dto, Employee author, Employee assignee);
}
