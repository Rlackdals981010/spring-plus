package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoPageResponse {

    private String title;
    private long managerCount;
    private long commentCount;

    public TodoPageResponse(String title, long managerCount, long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
