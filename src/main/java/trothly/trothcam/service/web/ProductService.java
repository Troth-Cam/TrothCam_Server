package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.image.ImageRepository;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.domain.product.PublicYn;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.ProductReqDto;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final HistoryRepository historyRepository;
    private final LikeProductRepository likeProductRepository;
    private final ImageRepository imageRepository;

    /* 공개 인증서 조회 */
    @Transactional(readOnly = true)
    public List<ProductsResDto> findPublicProducts(String webId) throws BaseException {
        List<Product> findProducts = productRepository.findAllByMember_WebIdAndPublicYn(webId, PublicYn.Y);
        if (findProducts == null || findProducts.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        // TODO: 2023/08/11 liked 여부 확인하는 로직 필요
        // TODO: 2023/08/11 createdAt LocalDateTime -> String 변환 로직 필요
        List<ProductsResDto> collect = findProducts.stream()
                .map(m -> new ProductsResDto(m.getTitle(), m.getMember().getWebId(), "20230605", m.getPrice(), true))
                .collect(Collectors.toList());

        return collect;
    }

    /* 상품 detail 화면 조회 */
    @Transactional(readOnly = true)
    public ProductDetailResDto findProductDetail(ProductReqDto req, Member member) {
        Boolean liked = false;

        Product product = productRepository.findById(req.getProductId()).orElseThrow(
                () -> new BadRequestException("존재하지 않는 상품입니다.")
        );

        // 조회수 갱신
        product.updateViews(product.getViews() + 1);

        Long likes = likeProductRepository.countByProductId(req.getProductId());

        //좋아요 여부
        Optional<LikeProduct> isLiked = likeProductRepository.findByProductIdAndMemberId(req.getProductId(), member.getId());

        if(isLiked.isPresent()) {
            liked = true;
        } else {
            liked = false;
        }

        List<History> histories = historyRepository.findAllByProductId(req.getProductId());

        return new ProductDetailResDto(req.getProductId(), product.getImage().getId(), product.getMember().getId(), product.getTitle(),
                product.getTags(), product.getPrice(), product.getDescription(),product.getViews(), likes, product.getPublicYn(), product.getCreatedAt(),
                product.getLastModifiedAt(), liked, histories);
    }
}
