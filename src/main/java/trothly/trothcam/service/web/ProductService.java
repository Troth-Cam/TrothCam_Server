package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.dto.app.CheckImgHashResDto;
import trothly.trothcam.dto.auth.web.CheckIdResDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenResDto;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.ProductReqDto;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.exception.custom.ProductNotFoundException;
import trothly.trothcam.exception.custom.SignupException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final HistoryRepository historyRepository;
    private final LikeProductRepository likeProductRepository;

//
//    /* 공개 인증서 조회 */
//    @Transactional(readOnly = true)
//    public List<Product> findPublicProducts(String webId) throws BaseException {
//        List<Product> findProducts = productRepository.findAllByMember_WebIdAndPublicYn_Y(webId);
//        if (findProducts == null || findProducts.isEmpty())
//            throw new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
//
//        return findProducts;
//    }

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
