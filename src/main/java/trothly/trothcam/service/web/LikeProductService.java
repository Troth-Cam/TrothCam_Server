package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.dto.web.LikeProductReqDto;
import trothly.trothcam.dto.web.LikeResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.custom.BadRequestException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeProductService {

    private final LikeProductRepository likeProductRepository;
    private final ProductRepository productRepository;

    // 좋아요 저장
    public LikeResDto saveLike(LikeProductReqDto req, Member member) throws BaseException {
        Optional<LikeProduct> like = likeProductRepository.findByProductIdAndMemberId(req.getProductId(), member.getId());

        if(like.isPresent()) {
            throw new BadRequestException("이미 좋아요를 누른 상품입니다.");
        }

        Product product = productRepository.findById(req.getProductId()).orElseThrow(
                () -> new BadRequestException("존재하지 않는 상품입니다.")
        );

        LikeProduct newLike = likeProductRepository.save(new LikeProduct(product, member));

        return new LikeResDto("좋아요 성공");
    }

    // 좋아요 삭제
    public LikeResDto deleteLike(LikeProductReqDto req, Member member) throws BaseException {
        LikeProduct likeProduct = likeProductRepository.findByProductIdAndMemberId(req.getProductId(), member.getId()).orElseThrow(
                () -> new BadRequestException("좋아요를 누르지 않은 상품입니다.")
        );

        likeProductRepository.deleteById(likeProduct.getId());

        return new LikeResDto("좋아요 해제 성공");
    }
}
