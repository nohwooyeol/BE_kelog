package com.kelog.kelog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCountResponseDto {

    private Long commentcount;

    private List<CommentResponseDto> responseDto; //댓글 리스트
}