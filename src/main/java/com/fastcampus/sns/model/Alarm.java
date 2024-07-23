package com.fastcampus.sns.model;

import com.fastcampus.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getId(),
                entity.getAlarmType(),
                entity.getAlarmArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public String getAlarmText() {
        return alarmType.getAlarmText();
    }
}

// N+1 문제
// 현재 AlarmEntity에는 User가 연관관계 매핑이 되어있고, user는 fetch type이 LAZY이지만,
// 실제적으로 이 클래스에 userName과 같은 컬럼이 있으면,
// fromEntity 할때 user에 접근하면서 user 조회 쿼리를 날리기 때문에 N+1문제가 발생한다.
// (따라서, LAZY가 근본적인 해결책은 아님)
// native로 query를 작성해서 처음부터 join fetch로 가져오도록 하는 방법이 더 명확한 해결방법이다.
// 그런데, 사실상 지금 알람 데이터를 가져올 때 user 정보는 불필요하기 때문에
// Alarm 클래스에서 user 관련 필드를 지워서 불필요한 연관관계를 끊어주는 것도 해결책 중 하나가 될 수 있다.
