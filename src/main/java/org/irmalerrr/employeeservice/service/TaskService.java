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
//todo -DONE- @saivanov: лишний перенос тут

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


        Employee author = employeeMap.get(dto.getAuthorId());//todo -DONE- @saivanov: ты getEmployeeFromDto вызываешь 2 раза, и еще потом getEmployeesFromDto. 3 запроса в бд летит) можно оптимизировать) собрать все нужные айдишники,сделать getEmployeesFromDto, и потом из полученого списка вытащить нужные тебе обьбекты по айди)
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
    //todo -DONE- @saivanov: Почитай про аннатацию @Transactional. для чего она нужна, как работает и когда используется.
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
        //todo -DONE- @saivanov: а как происходит сравнение тут? если сравниваются ссылки через ==, то может быть беда. Луше сделай  Set<Long> из йдишников targetViewers, и проверяй есть ли в этом сете viewer.getId().

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

    private List<Employee> findEmployeesByIds(List<Long> ids) { // todo -DONE- @saivanov: у тебя getEmployeeFromDto возвращает не ДТО) стоит переименовать метод
        if (CollectionUtils.isEmpty(ids)) { //todo -DONE- @saivanov: можно использовать CollectionUtils.isEmpty
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
            throw new EmployeeNotFoundException(String.format("Employees not found for IDs: %s", notFoundIds));//todo -DONE- @saivanov: старайся границы IF указывать({}) и сообщение подправь "EmployeeS not found" ну и мб стоит выводить, каких именнно айди не найдено?
        }
        return employees;
    }


//    private void updateViewers(List<Employee> sourceViewers, List<Employee> targetViewers) {  //todo -DONE: я вместо этого вернула эту логику в метод, не представляю как сунуть это в маппер, тут замена одних связей бд на другие- @saivanov:  ябы запихнул этот метод в Маппер, и сделал бы его дефолтным) как раз удобно будет тестить)
//        //todo -DONE- @saivanov: можно сразу на вход передавать список существующих вбюверов таски, чтоб обьект таски не тащить
//
//        Set<Long> targetViewerIds = targetViewers.stream().map(Employee::getId).collect(Collectors.toSet());
//        sourceViewers.removeIf(viewer -> !targetViewerIds.contains(viewer.getId())); //todo -DONE- @saivanov: а как происходит сравнение тут? если сравниваются ссылки через ==, то может быть беда. Луше сделай  Set<Long> из йдишников targetViewers, и проверяй есть ли в этом сете viewer.getId().
//
//        targetViewers.stream()
//                .filter(viewer -> !sourceViewers.contains(viewer))
//                .forEach(sourceViewers::add);
//    }
}

