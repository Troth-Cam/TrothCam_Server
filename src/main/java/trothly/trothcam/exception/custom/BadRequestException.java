package trothly.trothcam.exception.custom;

import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

public class BadRequestException extends BaseException {
    String message;

    public BadRequestException(String message) {
        super(ErrorCode._BAD_REQUEST);
        this.message = message;
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
