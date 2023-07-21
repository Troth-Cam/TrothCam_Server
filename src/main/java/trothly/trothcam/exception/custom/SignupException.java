package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class SignupException extends BaseException {

    private String message;

    public SignupException(String message) {
        super(ErrorCode.REQUEST_ERROR, message);
        this.message = message;
    }

    public SignupException(ErrorCode errorCode) {
        super(errorCode);
    }
}
