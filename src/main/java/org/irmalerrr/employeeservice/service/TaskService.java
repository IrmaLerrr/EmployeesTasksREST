package org.irmalerrr.employeeservice.service;

import lombok.RequiredArgsConstructor;
import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.exceptions.TaskNotFoundException;
import org.irmalerrr.employeeservice.mapper.TaskMapper;
import org.irmalerrr.employeeservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final EmployeeService employeeService;
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
                .orElseThrow(() -> new TaskNotFoundException(id)); //todo - DONE - текст в константы (убран в эксепшн)
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

        //todo - DONE - дважды использован маппер. видимо следующая строка лишняя
        entity = mapper.updateEntity(entity, dto, author, assignee);
        //todo - DONE - если в CreateTaskDto.viewersIds приходит полный список текущих id наблюдателей, то нужно часть удалить, часть добавить, те, что уже были добавлены не перезаписывать
        //todo - DONE - получение соответствующих viewers должно быть сделано в сервисе (можно эту логику вынести в отдельный метод)
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
        //todo: лучше писать тело if с фигурными скобками, даже если это одна строка (ниже тоже)
        if (id == null) return null;
        //todo: в класс можно добавить зависимость EmployeeRepository и работать с ним, а не через EmployeeService, если нет сложной логики
        return employeeService.getEmployee(id);
    }

    private List<Employee> getEmployeesFromDto(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        //todo: .toList() тоже на новую строку, либо оставить все на одной строке
        //todo: здесь будет запрос в БД через репозиторий для каждого элемента массива. лучше переделать с использованием findAll()
        return ids.stream()
                .map(employeeService::getEmployee).toList();
    }

    private void updateViewers(Task entity, List<Employee> targetViewers) {
        List<Employee> sourceViewers = entity.getViewers();

        sourceViewers.removeIf(viewer -> !targetViewers.contains(viewer));

        targetViewers.stream()
                .filter(viewer -> !sourceViewers.contains(viewer))
                .forEach(sourceViewers::add);
    }
}

