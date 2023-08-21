package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.*;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.service.web.ProductService;

import java.util.ArrayList;
import java.util.List;

import static trothly.trothcam.exception.base.ErrorCode.MEMBER_NOT_FOUND;
import static trothly.trothcam.exception.base.ErrorCode.REQUEST_ERROR;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    /* 인증서 조회 */
    @GetMapping("/products")
    public BaseResponse<List<ProductsResDto>> findPublicProducts(
            @RequestParam(value = "webToken") String webToken, @RequestParam(value = "public") String isPublic) {
        List<ProductsResDto> result;
        if (isPublic.equals("Y")) {
            result = productService.findPublicProducts(webToken);
        } else if (isPublic.equals("N")) {
            result = productService.findPrivateProducts(webToken);
        } else throw new BaseException(ErrorCode._BAD_REQUEST);

        if (result.isEmpty()) {
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return BaseResponse.onSuccess(result);
    }

    /* 상품 detail 화면 조회 - 로그인 0*/
    @GetMapping("/product-detail/{webId}")
    public BaseResponse<ProductDetailResDto> findProductDetailOn(@PathVariable String webId, @RequestBody ProductReqDto req, @AuthenticationPrincipal Member member) {
        if(req.getProductId() == null) {
            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
        }

        ProductDetailResDto res = productService.findProductDetailOn(req, member);
        return BaseResponse.onSuccess(res);
    }

    /* 상품 detail 화면 조회 - 로그인 X*/
    @GetMapping("/product-detail")
    public BaseResponse<ProductDetailResDto> findProductDetail(@RequestBody ProductReqDto req) {
        if(req.getProductId() == null) {
            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
        }

        ProductDetailResDto res = productService.findProductDetail(req);
        return BaseResponse.onSuccess(res);
    }

    /* 메인 화면 랭킹뷰 */
    @GetMapping("/product-ranking/{type}")
    public BaseResponse<List<ProductRankResDto>> findRanking(@PathVariable String type) {
        List<ProductRankResDto> result = new ArrayList<>();

        if(type.equals("top")) {
            result = productService.findProductRankTop();
        } else if(type.equals("latest")) {
            result = productService.findProductRankLatest();
        } else {
            throw new BaseException(REQUEST_ERROR);
        }

        return BaseResponse.onSuccess(result);
    }

    /* 메인 화면 페이징 처리 - 로그인x */
    @GetMapping("/product-ranking/{type}/{page}")
    public BaseResponse<ProductsPagingListResDto> getProducts(@PathVariable String type, @PathVariable int page) {
        if(type.equals("top") && page >= 0) {
            return BaseResponse.onSuccess(productService.getProductsTop(page));
        } else if (type.equals("latest") && page >= 0) {
            return BaseResponse.onSuccess(productService.getProductsLatest(page));
        } else {
            throw new BaseException(REQUEST_ERROR);
        }

    }

    /* 메인 화면 페이징 처리 - 로그인0 */
    @GetMapping("/{webId}/product-ranking/{type}/{page}")
    public BaseResponse<ProductsPagingListResDto> getProducts(@PathVariable String webId, @PathVariable String type, @PathVariable int page, @AuthenticationPrincipal Member member) {
        if(!member.getWebId().equals(webId)) {
            throw new BaseException(MEMBER_NOT_FOUND);
        }

        if(type.equals("top") && page >= 0) {
            return BaseResponse.onSuccess(productService.getProductsLikedTop(page, member));
        } else if (type.equals("latest") && page >= 0) {
            return BaseResponse.onSuccess(productService.getProductsLikedLatest(page, member));
        } else {
            throw new BaseException(REQUEST_ERROR);
        }
    }

    /* view all */
    @GetMapping("/view-all/{type}")
    public BaseResponse<List<ProductRankResDto>> findViewAll(@PathVariable String type) {
        List<ProductRankResDto> result = new ArrayList<>();

        if(type.equals("top")) {
            result = productService.findRankViewAllTop();
        } else if(type.equals("latest")) {
            result = productService.findRankViewAllLatest();
        } else {
            throw new BaseException(REQUEST_ERROR);
        }

        return BaseResponse.onSuccess(result);
    }
}
