package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.web.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    /* 공개 인증서 조회 */
    @GetMapping("/products")
    public BaseResponse<List<ProductsResDto>> findPublicProducts(@RequestParam(value = "web-id") String webId, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        List<ProductsResDto> result = productService.findPublicProducts(webId);
        return BaseResponse.onSuccess(result);
    }

    /* 상품 detail 화면 조회 */  // 구현은 했으나 테스트 아직 못해봄
//    @GetMapping("/product-detail")
//    public BaseResponse<ProductDetailResDto> findProductDetail(@RequestBody ProductReqDto req, @AuthenticationPrincipal Member member) {
//        if(req.getProductId() == null) {
//            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
//        }
//
//        ProductDetailResDto res = productService.findProductDetail(req, member);
//        return BaseResponse.onSuccess(res);
//    }
}
