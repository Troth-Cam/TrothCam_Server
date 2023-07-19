package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class LoginException extends BaseException {

    private String message;

    public LoginException(String message) {
        super(ErrorCode.REQUEST_ERROR, message);
        this.message = message;
    }

    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
