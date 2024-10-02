package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.dto.response.TodoPageResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TodoRepositoryImpl implements TodoRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;
    QTodo todo = QTodo.todo;

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
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(title!=null){
            booleanBuilder.and(todo.title.eq("%"+title+"%"));
        }
        if(nickName!=null){
            booleanBuilder.and(todo.user.nickname.eq("%"+nickName+"%"));
        }
        if(startDay!=null){
            booleanBuilder.and(todo.modifiedAt.goe(startDay));
        }
        if(endDay!=null){
            booleanBuilder.and(todo.modifiedAt.loe(endDay));
        }

        //- 일정에 대한 모든 정보가 아닌, 제목만 넣어주세요.
        //- 해당 일정의 담당자 수를 넣어주세요.
        //- 해당 일정의 총 댓글 개수를 넣어주세요.

        List<TodoPageResponse> results = jpaQueryFactory
                .select(Projections.constructor(TodoPageResponse.class,
                        todo.title,                                   // 제목
                        todo.managers.size().as("managerCount"),        // 담당자 수
                        todo.comments.size().as("commentCount")         // 총 댓글 개수
                ))
                .from(todo)
                .leftJoin(todo.managers).fetchJoin()
                .leftJoin(todo.comments).fetchJoin()
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(todo)
                .where(booleanBuilder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }


}
