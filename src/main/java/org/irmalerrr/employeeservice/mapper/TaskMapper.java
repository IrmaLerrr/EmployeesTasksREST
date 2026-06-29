package org.irmalerrr.employeeservice.mapper;


import lombok.RequiredArgsConstructor;
import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
@RequiredArgsConstructor
public abstract class TaskMapper { //todo @saivanov: не вижу потребности делать класс abstract, мб вернем interface?

    @Mapping(target = "viewers", source = "viewers") //todo @saivanov: название полей вроде одинаковое, так что можно строку эту и убрать(по желанию)
    public abstract TaskDto toDto(Task task);

    public abstract List<TaskDto> toDtoList(List<Task> task);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "viewers", source = "viewers")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) //todo @saivanov: поля с игнором выносим наверх, чтоб читаемость повысить)
    public abstract Task toEntity(CreateTaskDto dto, Employee author, Employee assignee, List<Employee> viewers);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "viewers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Task updateEntity(@MappingTarget Task entity, CreateTaskDto dto, Employee author, Employee assignee);
}
