package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class UnauthorizedException extends BaseException {

    private String message;

    public UnauthorizedException(String message){
        super(ErrorCode._UNAUTHORIZED, message);
        this.message = message;
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
