package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.ProductReqDto;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.service.web.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    /* 공개 인증서 조회 */
//    @GetMapping("/products")
//    public<T> BaseResponse<ProductsResDto> findPublicProducts(@RequestParam(value = "id") String id) {
//        List<Product> findProducts = productService.findPublicProducts(id);
//
//        // TODO: 2023/08/11 liked 여부 확인하는 로직 필요
//        // TODO: 2023/08/11 createdAt LocalDateTime -> String 변환 로직 필요
//        List<ProductsResDto> collect = findProducts.stream()
//                .map(m -> new ProductsResDto(m.getTitle(), m.getMember().getWebId(), "20230605", m.getPrice(), true))
//                .collect(Collectors.toList());
//
//        return new BaseResponse<ProductsResDto>(collect);
//    }

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
