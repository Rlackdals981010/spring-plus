package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Entity
@NoArgsConstructor
public class Log extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    public Log(String content) {
        this.content = content;
    }
}
