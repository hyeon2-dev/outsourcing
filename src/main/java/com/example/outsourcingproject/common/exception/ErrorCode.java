package com.example.outsourcingproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 인증 관련 코드
    NOT_FOUND_TOKEN("토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.FORBIDDEN),

    // 유저 관련 코드
    DUPLICATE_EMAIL("중복된 이메일이 있습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("비밀번호가 맞지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SAME_AS_OLD_PASSWORD("현재 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRMATION_MISMATCH("비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_ROLE("유효하지 않은 역할입니다.", HttpStatus.BAD_REQUEST),
    USER_ALREADY_DELETE("이미 탈퇴한 사용자입니다", HttpStatus.BAD_REQUEST),

    // 가게 관련 코드
    INVALID_CATEGORY_TYPE("유효하지 않은 카테고리 이름입니다." , HttpStatus.BAD_REQUEST),
    FORBIDDEN_STORE_CREATION("owner권한이 있는 사용자만 가게를 생성할 수 있습니다.", HttpStatus.FORBIDDEN),
    STORE_LIMIT_EXCEEDED("owner는 최대 3개의 가게만 운영할 수 있습니다.", HttpStatus.FORBIDDEN),
    STORE_NOT_FOUND("가게를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_STORE("본인 가게가 아닙니다.", HttpStatus.FORBIDDEN),
    STORE_ALREADY_DELETE("이미 페업한 가게입니다", HttpStatus.BAD_REQUEST),

    // 메뉴 관련 코드
    MENU_NOT_FOUND("메뉴를 찾을 수 없습니다." , HttpStatus.NOT_FOUND),
    MENU_ALREADY_DELETE("이미 삭제된 메뉴입니다.", HttpStatus.BAD_REQUEST),

    // 댓글 관련 코드
    COMMENT_NOT_FOUND("id에 맞는 댓글이 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_COMMENT("본인 댓글이 아니어서 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;
}
