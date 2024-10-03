package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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
import java.util.stream.Collectors;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.user.entity.QUser.user;

public class TodoRepositoryImpl implements TodoRepositoryCustom{

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
        // 검색 조건에 맞는 쿼리 작성
        List<TodoPageResponse> results = jpaQueryFactory
                .select(Projections.constructor(TodoPageResponse.class,
                        todo.title,
                        // 담당자 수 계산
                        JPAExpressions
                                .select(manager.count())
                                .from(manager)
                                .where(manager.todo.eq(todo)),
                        // 댓글 수 계산
                        JPAExpressions
                                .select(comment.count())
                                .from(comment)
                                .where(comment.todo.eq(todo))
                ))
                .from(todo)
                // 검색 조건 추가
                .leftJoin(todo.user, user)
                .where(
                        eqTitle(title),
                        eqStartDay(startDay),
                        eqEndDay(endDay),
                        eqNickName(nickName)
                )
                .orderBy(todo.createdAt.desc()) // 최신순으로 정렬
                .offset(pageable.getOffset())   // 페이징 처리
                .limit(pageable.getPageSize())
                .fetch();  // 결과 가져오기

        // 전체 카운트 계산 (페이징을 위해)
        long total = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        eqTitle(title),
                        eqStartDay(startDay),
                        eqEndDay(endDay),
                        eqNickName(nickName)
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


    private BooleanExpression eqTitle(String title){
        if(title==null){
            return null;
        }
        return todo.title.containsIgnoreCase(title);
    }
    private BooleanExpression eqNickName(String nickName){
        if(nickName==null){
            return null;
        }
        return JPAExpressions
                .selectFrom(manager)
                .where(manager.todo.eq(todo)
                        .and(manager.user.nickname.containsIgnoreCase(nickName)))
                .exists();
    }
    private BooleanExpression eqStartDay(LocalDateTime startDay){
        if(startDay==null){
            return null;
        }
        return todo.modifiedAt.goe(startDay);
    }
    private BooleanExpression eqEndDay(LocalDateTime endDay){
        if(endDay==null){
            return null;
        }
        return todo.modifiedAt.loe(endDay);
    }


}

