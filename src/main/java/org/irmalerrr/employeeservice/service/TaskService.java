package org.irmalerrr.employeeservice.service;

import lombok.RequiredArgsConstructor;
import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.exceptions.TaskNotFoundException;
import org.irmalerrr.employeeservice.mapper.TaskMapper;
import org.irmalerrr.employeeservice.repository.EmployeeRepository;
import org.irmalerrr.employeeservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;


    /**
     * Выдает задачу по ее id
     *
     * @param id - id задачи
     * @return TaskDto - DTO объект с данными задачи
     * @throws TaskNotFoundException если задача с указанным id не найдена
     */
    public TaskDto getTask(Long id) {
        Task entity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return mapper.toDto(entity);
    }

    /**
     * Выдает список всех задач.
     *
     * @return List<TaskDto> - список DTO объектов с данными задач
     */
    public List<TaskDto> getAllTasks() {
        return mapper.toDtoList(taskRepository.findAll());
    }

    /**
     * Создает задачу на основе полученных данных
     *
     * @param dto - данные задачи для создания
     * @return TaskDto - DTO объект с данными задачи
     * @throws EmployeeNotFoundException если сотрудник с указанным id не найден
     */
    public TaskDto createTask(CreateTaskDto dto) {
        Employee author = getEmployeeFromDto(dto.getAuthorId());
        Employee assignee = getEmployeeFromDto(dto.getAssigneeId());
        List<Employee> viewers = getEmployeesFromDto(dto.getViewersIds());
        Task entity = taskRepository.save(mapper.toEntity(dto, author, assignee, viewers));
        return mapper.toDto(entity);
    }

    /**
     * Обновляет задачу по id на основе полученных данных
     *
     * @param id  - id задачи, которую нужно обновить
     * @param dto - данные задачи для обновления
     * @return TaskDto - DTO объект с данными задачи
     * @throws TaskNotFoundException     если таска с указанным id не найдена
     * @throws EmployeeNotFoundException если сотрудник с указанным id не найден
     */
    public TaskDto updateTask(Long id, CreateTaskDto dto) {
        Task entity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        Employee author = getEmployeeFromDto(dto.getAuthorId());
        Employee assignee = getEmployeeFromDto(dto.getAssigneeId());
        List<Employee> viewers = getEmployeesFromDto(dto.getViewersIds());

        entity = mapper.updateEntity(entity, dto, author, assignee);
        updateViewers(entity, viewers);
        entity = taskRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Удаляет задачу по id
     *
     * @param id - id задачи, которую нужно удалить
     * @throws TaskNotFoundException если задача с указанным id не найдена
     */
    public void deleteTask(Long id) {
        Task target = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(target);
    }

    private Employee getEmployeeFromDto(Long id) {
        if (id == null) {
            return null;
        }
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    private List<Employee> getEmployeesFromDto(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Employee> employees = employeeRepository.findAllById(ids);
        if (ids.size() != employees.size()) throw new EmployeeNotFoundException();
        return employees;
    }

    private void updateViewers(Task entity, List<Employee> targetViewers) {
        List<Employee> sourceViewers = entity.getViewers();

        sourceViewers.removeIf(viewer -> !targetViewers.contains(viewer));

        targetViewers.stream()
                .filter(viewer -> !sourceViewers.contains(viewer))
                .forEach(sourceViewers::add);
    }
}

