package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class TodoPageResponse {

    private final String title;
    private final long managerCount;
    private final long commentCount;

    public TodoPageResponse(String title, long managerCount, long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
