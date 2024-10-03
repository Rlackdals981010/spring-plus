package org.example.expert.domain.log.service;

import lombok.AllArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(User user, Todo todo){
        long userId = user.getId();
        long todoId = todo.getId();
        String content = userId+"번 유저가 "+todoId+"번 일정에 매니저를 등록 시도";
        System.out.println(content);
        Log log = new Log(content);
        logRepository.save(log);
    }
}
