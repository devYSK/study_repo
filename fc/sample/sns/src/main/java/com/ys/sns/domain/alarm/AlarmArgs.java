package com.ys.sns.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {
    // user who occur alarm
    private Integer fromUserId;
    private Integer targetId;
}
