package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class TokenExpiredException extends BaseException {

    private String message;

    public TokenExpiredException(String message){
        super(ErrorCode._BAD_REQUEST,message);
        this.message = message;
    }

    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
