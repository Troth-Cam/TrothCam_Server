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

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
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
        List<Product> findProducts = productRepository.findAllByMember_WebIdAndPublicYn(webId, PublicYn.Y);

        if (findProducts == null || findProducts.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        List<ProductsResDto> result = new ArrayList<>();
        for (int i = 0; i < findProducts.size(); i++) {
            Product p = findProducts.get(i);
            LocalDateTime soldAt = historyRepository.findTopByProduct_IdOrderBySoldAt(p.getId())
                    .orElse(p.getLastModifiedAt());
            boolean isLiked = likeProductRepository.existsByProduct_IdAndMember_Id(p.getId(), p.getMember().getId());

            ProductsResDto dto = new ProductsResDto(p.getTitle(), p.getMember().getWebId(),
                    soldAt.format(DateTimeFormatter.ofPattern("YYYYMMdd")), p.getPrice(), isLiked);

            result.add(dto);
        }

        return result;
    }

    /* 비공개 인증서 조회 */
    @Transactional(readOnly = true)
    public List<ProductsResDto> findPrivateProducts(String webId) throws BaseException {
        List<Product> findProducts = productRepository.findAllByMember_WebIdAndPublicYn(webId, PublicYn.N);

        if (findProducts == null || findProducts.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        List<ProductsResDto> result = new ArrayList<>();
        for (int i = 0; i < findProducts.size(); i++) {
            Product p = findProducts.get(i);
            LocalDateTime soldAt = p.getLastModifiedAt();
            boolean isLiked = likeProductRepository.existsByProduct_IdAndMember_Id(p.getId(), p.getMember().getId());
            Long price = p.getPrice();
            if (price == null) {
                price = 0L;
            }

            ProductsResDto dto = new ProductsResDto(p.getTitle(), p.getMember().getWebId(),
                    soldAt.format(DateTimeFormatter.ofPattern("YYYYMMdd")), price, isLiked);

            result.add(dto);
        }

        return result;
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
//    @Transactional
//    public List<ProductRankResDto> findProductRankTop() {
//        List<Tuple> productRankDtos = productRepository.findProductRandDto();
//
//        List<ProductRankResDto> rankResDtos = productRankDtos.stream()
//                .map(t -> {
//                    Optional<Member> owner = memberRepository.findById(Long.valueOf(t.get(3, Integer.class)));
//                    Optional<Image> image = imageRepository.findById(Long.valueOf(t.get(6, Integer.class)));
//
//                    return new ProductRankResDto(
//                            Long.valueOf(t.get(0, Integer.class)),
//                            Long.valueOf(t.get(1, Integer.class)),
//                            owner.get().getWebToken(),
//                            owner.get().getName(),
//                            image.get().getMember().getWebToken(),
//                            t.get(7, String.class),
//                            t.get(8, Integer.class),
//                            image.get().getImageUrl(),
//                            Long.valueOf(t.get(4, Integer.class)),
//                            t.get(5, LocalDateTime.class)
//                    );
//                }).collect(Collectors.toList());
//
////        List<ProductRankResDto> rankResDtos = new ArrayList<>();
////
////        for (ProductRepository.ProductTop productRank : productRankDtos) {
////            Optional<Member> owner = memberRepository.findById(productRank.getBuyerId());
////            Optional<Image> image = imageRepository.findById(productRank.getImageId());
////
////            if(owner.isPresent() && image.isPresent()) {
////                ProductRankResDto productRankResDto = new ProductRankResDto(productRank.getHistoryId(), productRank.getProductId(), owner.get().getWebToken(), owner.get().getName(),
////                        image.get().getMember().getWebToken(), productRank.getTitle(), productRank.getTags(), image.get().getImageUrl(),
////                        productRank.getPrice(), productRank.getSoldAt());
////
////                rankResDtos.add(productRankResDto);
////            }
////        }
//
//        return rankResDtos;
//    }

    /* 메인 랭킹 latest 조회 */
    @Transactional
    public List<ProductRankResDto> findProductRankLatest() {
        List<ProductRepository.ProductTop> productLatestDtos = productRepository.findProductLatestDto();

        List<ProductRankResDto> latestResDtos = new ArrayList<>();

        for (ProductRepository.ProductTop productLatest : productLatestDtos) {
            Optional<Member> owner = memberRepository.findById(productLatest.getBuyerId());
            Optional<Image> image = imageRepository.findById(productLatest.getImageId());

            if(owner.isPresent() && image.isPresent()) {
                ProductRankResDto productRankResDto = new ProductRankResDto(productLatest.getHistoryId(), productLatest.getProductId(), owner.get().getWebToken(), owner.get().getName(),
                        image.get().getMember().getWebToken(), productLatest.getTitle(), productLatest.getTags(), image.get().getImageUrl(),
                        productLatest.getPrice(), productLatest.getSoldAt());

                latestResDtos.add(productRankResDto);
            }
        }

        return latestResDtos;
    }

    /* view all top 조회 */
    @Transactional
    public List<ProductRankResDto> findRankViewAllTop() {
        List<ProductRepository.ProductTop> rankViewALlTopDtos = productRepository.findRankViewAllDto();

        List<ProductRankResDto> rankAllTopDtos = new ArrayList<>();

        for (ProductRepository.ProductTop rankViewAllTop : rankViewALlTopDtos) {
            Optional<Member> owner = memberRepository.findById(rankViewAllTop.getBuyerId());
            Optional<Image> image = imageRepository.findById(rankViewAllTop.getImageId());

            if(owner.isPresent() && image.isPresent()) {
                ProductRankResDto productRankResDto = new ProductRankResDto(rankViewAllTop.getHistoryId(), rankViewAllTop.getProductId(), owner.get().getWebToken(), owner.get().getName(),
                        image.get().getMember().getWebToken(), rankViewAllTop.getTitle(), rankViewAllTop.getTags(), image.get().getImageUrl(),
                        rankViewAllTop.getPrice(), rankViewAllTop.getSoldAt());

                rankAllTopDtos.add(productRankResDto);
            }
        }

        return rankAllTopDtos;
    }

    /* view all latest 조회 */
    @Transactional
    public List<ProductRankResDto> findRankViewAllLatest() {
        List<ProductRepository.ProductTop> rankViewALlLatestDtos = productRepository.findRankLatestViewAllDto();

        List<ProductRankResDto> rankAllLatestDtos = new ArrayList<>();

        for (ProductRepository.ProductTop rankViewAllLatest : rankViewALlLatestDtos) {
            Optional<Member> owner = memberRepository.findById(rankViewAllLatest.getBuyerId());
            Optional<Image> image = imageRepository.findById(rankViewAllLatest.getImageId());

            if(owner.isPresent() && image.isPresent()) {
                ProductRankResDto productRankResDto = new ProductRankResDto(rankViewAllLatest.getHistoryId(), rankViewAllLatest.getProductId(), owner.get().getWebToken(), owner.get().getName(),
                        image.get().getMember().getWebToken(), rankViewAllLatest.getTitle(), rankViewAllLatest.getTags(), image.get().getImageUrl(),
                        rankViewAllLatest.getPrice(), rankViewAllLatest.getSoldAt());

                rankAllLatestDtos.add(productRankResDto);
            }
        }

        return rankAllLatestDtos;
    }
}