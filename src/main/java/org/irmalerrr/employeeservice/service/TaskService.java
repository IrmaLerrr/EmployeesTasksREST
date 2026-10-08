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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Transactional
    public TaskDto createTask(CreateTaskDto dto) {
        Set<Long> allIds = new HashSet<>();
        allIds.add(dto.getAuthorId());
        allIds.add(dto.getAssigneeId());
        if (dto.getViewersIds() != null) {
            allIds.addAll(dto.getViewersIds());
        }
        allIds.remove(null);

        List<Employee> allEmployees = findEmployeesByIds(new ArrayList<>(allIds));

        Map<Long, Employee> employeeMap = allEmployees.stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity()));


        Employee author = employeeMap.get(dto.getAuthorId());
        Employee assignee = employeeMap.get(dto.getAssigneeId());
        List<Employee> viewers = new ArrayList<>();
        if (dto.getViewersIds() != null) {
            viewers = dto.getViewersIds().stream()
                    .map(employeeMap::get)
                    .filter(Objects::nonNull)
                    .toList();
        }

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
    @Transactional
    public TaskDto updateTask(Long id, CreateTaskDto dto) {
        Task entity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        Set<Long> allIds = new HashSet<>();
        allIds.add(dto.getAuthorId());
        allIds.add(dto.getAssigneeId());
        if (dto.getViewersIds() != null) {
            allIds.addAll(dto.getViewersIds());
        }
        allIds.remove(null);

        List<Employee> allEmployees = findEmployeesByIds(new ArrayList<>(allIds));

        Map<Long, Employee> employeeMap = allEmployees.stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity()));

        Employee author = employeeMap.get(dto.getAuthorId());
        Employee assignee = employeeMap.get(dto.getAssigneeId());
        List<Employee> targetViewers = new ArrayList<>();
        if (dto.getViewersIds() != null) {
            targetViewers = dto.getViewersIds().stream()
                    .map(employeeMap::get)
                    .filter(Objects::nonNull)
                    .toList();
        }

        entity = mapper.updateEntity(entity, dto, author, assignee);
        List<Employee> sourceViewers = entity.getViewers();

        Set<Long> targetViewerIds = targetViewers.stream().map(Employee::getId).collect(Collectors.toSet());
        sourceViewers.removeIf(viewer -> !targetViewerIds.contains(viewer.getId()));

        targetViewers.stream()
                .filter(viewer -> !sourceViewers.contains(viewer))
                .forEach(sourceViewers::add);

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

    private List<Employee> findEmployeesByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Employee> employees = employeeRepository.findAllById(ids);
        if (ids.size() != employees.size()) {
            List<Long> foundIds = employees.stream()
                    .map(Employee::getId)
                    .toList();

            List<Long> notFoundIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            if (notFoundIds.size() == 1) throw new EmployeeNotFoundException(notFoundIds.getFirst());
            throw new EmployeeNotFoundException(String.format("Employees not found for IDs: %s", notFoundIds));
        }
        return employees;
    }
}

