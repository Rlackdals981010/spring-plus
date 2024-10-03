package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class TodoPageResponse {

    private final String title;
    private final Long managerCount;
    private final Long commentCount;

    public TodoPageResponse(String title, Long managerCount, Long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
