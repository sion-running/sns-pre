package com.fastcampus.sns.model.entity;

import com.fastcampus.sns.model.AlarmArgs;
import com.fastcampus.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE  \"alarm\" SET deleted_at = NOW() where id = ?") // delete 쿼리 날아갈 때
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private AlarmArgs alarmArgs;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args) {
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        entity.setAlarmArgs(args);
        return entity;
    }
}
