package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.ProductReqDto;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.service.web.ProductService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    // TODO: 2023/08/13 응답 값이 비어있음. 엥? 로그도 안찍힘.. JPA 문제? query 직접 짜보기.
    /* 공개 인증서 조회 */
    @GetMapping("/products")
    public BaseResponse<List<ProductsResDto>> findPublicProducts(@RequestParam(value = "web-id") String webId) {
        List<ProductsResDto> result = productService.findPublicProducts(webId);
        log.trace("인증서 가져오기 controller");
        if (result.isEmpty()) {
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return BaseResponse.onSuccess(result);
    }

    /* 상품 detail 화면 조회 */  // 구현은 했으나 테스트 아직 못해봄
    @GetMapping("/product-detail")
    public BaseResponse<ProductDetailResDto> findProductDetail(@RequestBody ProductReqDto req, @AuthenticationPrincipal Member member) {
        if(req.getProductId() == null) {
            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
        }

        ProductDetailResDto res = productService.findProductDetail(req, member);
        return BaseResponse.onSuccess(res);
    }
}
