package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class InvalidProviderException extends BaseException {

    private String message;

    public InvalidProviderException(String message){
        super(ErrorCode.INVALID_PROVIDER, message);
        this.message = message;
    }

    public InvalidProviderException(ErrorCode errorCode) {
        super(errorCode);
    }
}
