package com.kakaopay.restfulwebservice.exception;

public enum ExceptionStatus {
    ERORR500(500, "뿌리기당 한 사용자는 한번만 받을 수 있습니다."),
    ERORR501(501, "자신이 뿌리기한 건은 자신이 받을 수 없습니다."),
    ERORR502(502, "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."),
    ERORR503(503, "뿌린 건은 10분간만 유효합니다."),
    ERORR504(504, "뿌리기당 한 사용자는 한번만 받을 수 있습니다."),
    ERORR505(505, "뿌린 사람 자신만 조회를 할 수 있습니다."),
    ERORR506(506, "뿌린 건에 대한 조회는 7일 동안 할 수 있습니다."),
    ERORR507(507, "서버 요청 에러입니다.");

    private final int code;
    private final String msg;

    ExceptionStatus(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }
    public String getDescription(){
        return msg;
    }
}
