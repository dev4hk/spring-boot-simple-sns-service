package com.example.simple_sns_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationArgument {
    private Integer fromUserId;
    private Integer targetId;
}
