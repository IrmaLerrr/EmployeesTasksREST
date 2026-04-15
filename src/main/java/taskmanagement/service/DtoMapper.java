package taskmanagement.service;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import taskmanagement.dto.*;
import taskmanagement.model.Employee;
import taskmanagement.model.Task;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configureMappings();
    }

    private void configureMappings() {
        Converter<Employee, EmployeeDTOResponseShort> employeeToShortConverter =
                context -> {
                    Employee employee = context.getSource();
                    if (employee == null) return null;
                    return new EmployeeDTOResponseShort()
                            .setId(employee.getId())
                            .setFirstName(employee.getFirstName())
                            .setLastName(employee.getLastName());
                };

        modelMapper.addConverter(employeeToShortConverter);

        modelMapper.typeMap(Task.class, TaskDTOResponse.class)
                .addMappings(mapper -> {
                    mapper.using(employeeToShortConverter).map(Task::getAuthor, TaskDTOResponse::setAuthor);
                    mapper.using(employeeToShortConverter).map(Task::getAssignee, TaskDTOResponse::setAssignee);
                });
    }

    public Employee dtoToEntity(EmployeeDTORequest dto) {
        return modelMapper.map(dto, Employee.class);
    }

    public Task dtoToEntity(TaskDTORequest dto) {
        return modelMapper.map(dto, Task.class);
    }

    public EmployeeDTOResponse entityToDto(Employee entity) {
        return modelMapper.map(entity, EmployeeDTOResponse.class);
    }

    public List<EmployeeDTOResponse> entityToDtoEmployee(List<Employee> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public EmployeeDTOResponseShort entityToDtoShort(Employee entity) {
        return modelMapper.map(entity, EmployeeDTOResponseShort.class);
    }

    public TaskDTOResponse entityToDto(Task entity) {
        return modelMapper.map(entity, TaskDTOResponse.class);
    }

    public List<TaskDTOResponse> entityToDtoTask(List<Task> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

