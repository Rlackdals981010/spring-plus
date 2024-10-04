package org.example.expert.domain.todo.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.dto.response.TodoPageResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;


public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;
    QTodo todo = QTodo.todo;

    public TodoRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public Page<TodoPageResponse> findAllByTitleOrDateRangeOrManagerNickName(String title, LocalDateTime startDay, LocalDateTime endDay, String nickName, Pageable pageable) {
//        List<Todo> todos = jpaQueryFactory
//                .select(todo)
//                .distinct() // 중복 호출 삭제
//                .from(todo)
//                .join(todo.managers, manager).fetchJoin()
////                .join(todo.comments, comment)
//                .where(
//                        eqTitle(title)
//                        , (eqNickName(nickName))
//                        , (eqStartDay(startDay))
//                        , (eqEndDay(endDay))
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        List<TodoPageResponse> result = todos.stream()
//                .map(todo ->
//                        new TodoPageResponse(
//                                todo.getTitle(),
//                                (long) todo.getManagers().size(),
//                                (long) todo.getComments().size()
//                        )
//                ).toList();

        List<TodoPageResponse> result = jpaQueryFactory
                .select(Projections.constructor(TodoPageResponse.class,
                        todo.title,
                        todo.managers.size().longValue(), // 매니저 수
                        todo.comments.size().longValue()  // 댓글 수
                ))
                .distinct()
                .from(todo)
                .join(todo.managers, manager)
                .where(
                        eqTitle(title),
                        eqNickName(nickName),
                        eqStartDay(startDay),
                        eqEndDay(endDay)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count) // select count *
                .from(todo)
                .fetchOne();


        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression eqTitle(String title) {
        return (title != null && !title.isEmpty()) ? todo.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression eqNickName(String nickName) {
        return (nickName != null && !nickName.isEmpty()) ? manager.user.nickname.containsIgnoreCase(nickName) : null;
    }

    private BooleanExpression eqStartDay(LocalDateTime startDay) {
        return startDay != null ? todo.modifiedAt.goe(startDay) : null;
    }

    private BooleanExpression eqEndDay(LocalDateTime endDay) {
        return endDay != null ? todo.modifiedAt.loe(endDay) : null;
    }


}

