package taskmanagement.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import taskmanagement.dto.EmployeeDTO;
import taskmanagement.dto.TaskDTO;
import taskmanagement.model.Employee;
import taskmanagement.model.Task;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    Employee dtoToEntity(EmployeeDTO dto) {
        return modelMapper.map(dto, Employee.class);
    }

    Task dtoToEntity(TaskDTO dto) {
        return modelMapper.map(dto, Task.class);
    }

//    EmployeeDTO EntityToDto(Employee entity) {
//        return modelMapper.map(entity, EmployeeDTO.class);
//    }

}

