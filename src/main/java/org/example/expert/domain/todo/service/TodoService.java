package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.client.WeatherClient;
import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoPageResponse;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    //        Page<Todo> todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);

    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDate startDay, LocalDate endDay) {
        Pageable pageable = PageRequest.of(page - 1, size);

        LocalDateTime startDateTime = (startDay != null) ? startDay.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDay != null) ? endDay.atTime(LocalTime.MAX) : null;

        Page<Todo> todos = todoRepository.findAllByOrderAndWeatherOrDateRangeByModifiedAtDesc(pageable,weather,startDateTime,endDateTime);

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    public Page<TodoPageResponse> getNewTodos(int page, int size, String title,  LocalDate startDay, LocalDate endDay, String nickName) {
        Pageable pageable = PageRequest.of(page - 1, size);

        LocalDateTime startDateTime = (startDay != null) ? startDay.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDay != null) ? endDay.atTime(LocalTime.MAX) : null;

        Page<TodoPageResponse> todos = todoRepository.findAllByTitleOrDateRangeOrManagerNickName(title, startDateTime, endDateTime, nickName, pageable);
        return todos.map(todo -> new TodoPageResponse(
                todo.getTitle(),
                todo.getManagerCount(),
                todo.getCommentCount()
        ));
    }
}
