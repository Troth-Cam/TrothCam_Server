package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.image.ImageRepository;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.domain.product.PublicYn;
import trothly.trothcam.dto.web.*;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final HistoryRepository historyRepository;
    private final LikeProductRepository likeProductRepository;
    private final ImageRepository imageRepository;
    private final HistoryService historyService;
    private final MemberRepository memberRepository;

    /* 공개 인증서 조회 */
    @Transactional(readOnly = true)
    public List<ProductsResDto> findPublicProducts(String webId) throws BaseException {
        log.trace("인증서 가져오기 service1");
        List<Product> findProducts = productRepository.findAllByMember_WebIdAndPublicYn(webId, PublicYn.Y);
        log.trace("인증서 가져오기 service2", findProducts.toString());
        if (findProducts == null || findProducts.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        // TODO: 2023/08/11 liked 여부 확인하는 로직 필요
        // TODO: 2023/08/11 createdAt LocalDateTime -> String 변환 로직 필요
        List<ProductsResDto> collect = findProducts.stream()
                .map(m -> new ProductsResDto(m.getTitle(), m.getMember().getWebId(), "20230605", m.getPrice(), true))
                .collect(Collectors.toList());

        return collect;
    }

    /* 상품 detail 화면 조회 - 로그인 0 */
    @Transactional
    public ProductDetailResDto findProductDetailOn(ProductReqDto req, Member member) {
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

        List<HistoryDto> historyDto = historyService.findAllHistory(req);

        return new ProductDetailResDto(req.getProductId(), product.getImage().getId(), product.getMember().getId(), product.getTitle(),
                product.getTags(), product.getPrice(), product.getDescription(),product.getViews(), likes, product.getPublicYn(), product.getCreatedAt(),
                product.getLastModifiedAt(), liked, historyDto);
    }

    /* 상품 detail 화면 조회 - 로그인 x */
    @Transactional
    public ProductDetailResDto findProductDetail(ProductReqDto req) {
        Boolean liked = false;

        Product product = productRepository.findById(req.getProductId()).orElseThrow(
                () -> new BadRequestException("존재하지 않는 상품입니다.")
        );

        // 조회수 갱신
        product.updateViews(product.getViews() + 1);

        Long likes = likeProductRepository.countByProductId(req.getProductId());

        List<HistoryDto> historyDto = historyService.findAllHistory(req);

        return new ProductDetailResDto(req.getProductId(), product.getImage().getId(), product.getMember().getId(), product.getTitle(),
                product.getTags(), product.getPrice(), product.getDescription(),product.getViews(), likes, product.getPublicYn(), product.getCreatedAt(),
                product.getLastModifiedAt(), liked, historyDto);
    }

    /* 메인 랭킹 top 조회 */
    @Transactional
    public List<ProductRankResDto> findProductRankTop() {
        List<ProductRepository.ProductTop> productRankDtos = productRepository.findProductRandDto();

        List<ProductRankResDto> rankResDtos = new ArrayList<>();

        for (ProductRepository.ProductTop productRank : productRankDtos) {
            Optional<Member> owner = memberRepository.findById(productRank.getBuyerId());
            Optional<Image> image = imageRepository.findById(productRank.getImageId());

            log.info(owner.get().getId().toString());
            log.info(image.get().getId().toString());

            if(owner.isPresent() && image.isPresent()) {
                ProductRankResDto productRankResDto = new ProductRankResDto(productRank.getHistoryId(), productRank.getProductId(), owner.get().getWebToken(), owner.get().getName(),
                        image.get().getMember().getWebToken(), productRank.getTitle(), productRank.getTags(), image.get().getImageUrl(),
                        productRank.getPrice(), productRank.getSoldAt());

                rankResDtos.add(productRankResDto);
            }
        }

        return rankResDtos;
    }

    /* 메인 랭킹 latest 조회 */
    @Transactional
    public List<ProductRankResDto> findProductRankLatest() {
        List<ProductRepository.ProductTop> productLatestDtos = productRepository.findProductLatestDto();

        List<ProductRankResDto> latestResDtos = new ArrayList<>();

        for (ProductRepository.ProductTop productLatest : productLatestDtos) {
            Optional<Member> owner = memberRepository.findById(productLatest.getBuyerId());
            Optional<Image> image = imageRepository.findById(productLatest.getImageId());

            log.info(owner.get().getId().toString());
            log.info(image.get().getId().toString());

            if(owner.isPresent() && image.isPresent()) {
                ProductRankResDto productRankResDto = new ProductRankResDto(productLatest.getHistoryId(), productLatest.getProductId(), owner.get().getWebToken(), owner.get().getName(),
                        image.get().getMember().getWebToken(), productLatest.getTitle(), productLatest.getTags(), image.get().getImageUrl(),
                        productLatest.getPrice(), productLatest.getSoldAt());

                latestResDtos.add(productRankResDto);
            }
        }

        return latestResDtos;
    }
}