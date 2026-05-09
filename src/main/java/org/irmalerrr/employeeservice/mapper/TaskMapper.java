package org.irmalerrr.employeeservice.mapper;


import lombok.RequiredArgsConstructor;
import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.service.EmployeeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
@RequiredArgsConstructor
public abstract class TaskMapper {
    //todo: в маппер не нужно внедрять зависимость от сервиса. в маппер нужно подавать объект, который будет мапиться, а его получение должно быть в самом сервисе
    @Autowired
    protected EmployeeService employeeService;

    //todo - DONE - здесь маппинги можно не указывать, если совпадают наименования
    @Mapping(target = "viewers", source = "viewers")
    public abstract TaskDto toDto(Task task);

    //todo - DONE - этот метод при наличии метода маппинга классов без листов (в данном случае toDto()) использует его в имплементации, указывать маппинги не нужно
    public abstract List<TaskDto> toDtoList(List<Task> task);

    //todo: в аргументы этого метода нужно подать author, assignee и viewers в качестве аргументов
    @Mapping(target = "author", expression = "java(getEmployeeFromDto(dto.getAuthorId()))")
    @Mapping(target = "assignee", expression = "java(getEmployeeFromDto(dto.getAssigneeId()))")
    @Mapping(target = "viewers", expression = "java(getEmployeesFromDto(dto.getViewersIds()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Task toEntity(CreateTaskDto dto);

    //todo: в аргументы этого метода нужно подать author, assignee и viewers в качестве аргументов
    @Mapping(target = "author", expression = "java(getEmployeeFromDto(dto.getAuthorId()))")
    @Mapping(target = "assignee", expression = "java(getEmployeeFromDto(dto.getAssigneeId()))")
    @Mapping(target = "viewers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Task updateEntity(@MappingTarget Task entity, CreateTaskDto dto);

    //todo - DONE - логику и получение информации из БД через репозиторий лучше не добавлять в маппер. маппер только мапит из одного состояния в другое
    //todo - DONE - здесь пожалуй лучше будет найти соответствующие Employee в рамках сервиса, а в методы маппера передавать дополнительные аргументы
    protected Employee getEmployeeFromDto(Long id) {
        if (id == null) return null;
        return employeeService.getEmployee(id);
    }

    public List<Employee> getEmployeesFromDto(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        return ids.stream()
                .map(id -> employeeService.getEmployee(id)).toList();
    }
}
