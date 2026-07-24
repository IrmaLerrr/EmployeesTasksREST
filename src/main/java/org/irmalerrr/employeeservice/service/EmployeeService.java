package org.irmalerrr.employeeservice.service;

import lombok.RequiredArgsConstructor;
import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.mapper.EmployeeMapper;
import org.irmalerrr.employeeservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    /**
     * Выдает сотрудника по его id
     *
     * @param id - id сотрудника
     * @return EmployeeDto - DTO объект с данными сотрудника
     * @throws EmployeeNotFoundException если сотрудник с указанным id не найден
     */
    public EmployeeDto getEmployee(Long id) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return mapper.toDto(entity);
    }

    /**
     * Выдает список всех сотрудников.
     *
     * @return List<EmployeeDto> - список DTO объектов с данными сотрудников
     */
    public List<EmployeeDto> getAllEmployees() {
        return mapper.toDtoList(employeeRepository.findAll());
    }

    /**
     * Создает сотрудника на основе полученных данных
     *
     * @param dto - данные сотрудника для создания
     * @return EmployeeDto - DTO объект с данными сотрудника
     */
    public EmployeeDto createEmployee(CreateEmployeeDto dto) {
        Employee entity = employeeRepository.save(mapper.toEntity(dto));
        return mapper.toDto(entity);
    }

    /**
     * Обновляет сотрудника по id на основе полученных данных
     *
     * @param id - id сотрудника, которого нужно обновить
     * @param dto - данные сотрудника для обновления
     * @return EmployeeDto - DTO объект с данными сотрудника
     * @throws EmployeeNotFoundException если сотрудник с указанным id не найден
     */
    public EmployeeDto updateEmployee(Long id, CreateEmployeeDto dto) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        entity = employeeRepository.save(mapper.updateEntity(entity, dto));
        return mapper.toDto(entity);
    }

    /**
     * Удаляет сотрудника по id
     *
     * @param id - id сотрудника, которого нужно удалить
     * @throws EmployeeNotFoundException если сотрудник с указанным id не найден
     */
    public void deleteEmployee(Long id) {
        Employee target = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        employeeRepository.delete(target);  //todo -ОТВЕТ: удаляются связи, но не таски, но если authorId NULL нужно при апдейте нового автора добавить, в чем то это костыль - @saivanov: при удалении, обрати внимание, что происходит с связанными обьектами) они тоже удаляются или остаются
    }
}
