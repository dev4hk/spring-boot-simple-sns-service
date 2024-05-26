package com.example.simple_sns_service.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PostCommentRequest {
    private String comment;
}
