package com.ys.sns.domain.alarm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {
    private AlarmType type;
    private AlarmArgs args;
    private Integer receiverUserId = 1;
}
