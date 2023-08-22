package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.dto.web.LikeResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.custom.BadRequestException;

import java.util.Optional;

import static trothly.trothcam.exception.base.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeProductService {

    private final LikeProductRepository likeProductRepository;
    private final ProductRepository productRepository;

    // 좋아요 저장
    public LikeResDto saveLike(Long productId, Member member) {
        Optional<LikeProduct> like = likeProductRepository.findByProductIdAndMemberId(productId, member.getId());

        if(like.isPresent()) {
            throw new BaseException(ALREADY_LIKED);
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BaseException(PRODUCT_IS_NOT_FOUND)
        );

        LikeProduct newLike = likeProductRepository.save(new LikeProduct(product, member));

        return new LikeResDto("좋아요 성공");
    }

    // 좋아요 삭제
    public LikeResDto deleteLike(Long productId, Member member) {
        LikeProduct likeProduct = likeProductRepository.findByProductIdAndMemberId(productId, member.getId()).orElseThrow(
                () -> new BaseException(NOT_LIKED)
        );

        likeProductRepository.deleteById(likeProduct.getId());

        return new LikeResDto("좋아요 해제 성공");
    }
}
