package trothly.trothcam.exception.custom;

import lombok.Getter;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

@Getter
public class ProductNotFoundException extends BaseException {

    private String message;

    public ProductNotFoundException(String message){
        super(ErrorCode.PRODUCT_NOT_FOUND, message);
        this.message = message;
    }

    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
