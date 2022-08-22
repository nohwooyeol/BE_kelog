package com.kelog.kelog.request;

import com.kelog.kelog.response.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private String comment;
}
