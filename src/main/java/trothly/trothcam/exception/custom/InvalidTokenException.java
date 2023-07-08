package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class InvalidTokenException extends BaseException {

    private String message;

    public InvalidTokenException(String message){
        super(ErrorCode._BAD_REQUEST,message);
        this.message = message;
    }

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
