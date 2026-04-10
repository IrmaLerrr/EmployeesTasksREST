package taskmanagement.service;

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

    public List<EmployeeDTOResponse> entityToDto(List<Employee> entities) {
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

}

