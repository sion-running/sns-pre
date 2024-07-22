package com.fastcampus.sns.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AlarmArgs {
    private Integer fromUserId; // 알람을 발생시킨 사람
    private Integer targetId;

    @JsonCreator
    public AlarmArgs(@JsonProperty("fromUserId") Integer fromUserId,
                     @JsonProperty("targetId") Integer targetId) {
        this.fromUserId = fromUserId;
        this.targetId = targetId;
    }
}
