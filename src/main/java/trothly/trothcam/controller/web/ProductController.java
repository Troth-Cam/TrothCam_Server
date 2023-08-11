package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseResponse;
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

}
