package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.domain.product.PublicYn;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.dto.web.certificate.PrivateDetailDto;
import trothly.trothcam.dto.web.certificate.ProductDto;
import trothly.trothcam.dto.web.certificate.PublicDetailDto;
import trothly.trothcam.dto.web.certificate.PublicResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {
    private final ProductRepository productRepository;
    private final LikeProductRepository likeProductRepository;
    private final HistoryRepository historyRepository;

    // 공개 인증서 비공개 인증서로 변환 (비공개하기[판매취소] 클릭 시)
    public List<ProductDto> getPrivateCertificates(Member member, Long productId) {
        Optional<Product> getProduct = productRepository.findById(productId);
        if(getProduct.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        Product product = getProduct.get();
        product.updatePublicYn(PublicYn.N);   // 1. 해당 인증서 비공개로 전환
        productRepository.save(product);

        List<Product> productList = productRepository.findAllByMemberAndPublicYn(member, PublicYn.N);   // 2. 비공개 리스트 조회
        List<ProductDto> productDtoList = productList.stream()
                .map(p -> {
                    Member getMember = p.getMember();
                    String webToken = getMember.getWebToken();  // 1. webToken 가져오기

                    // 2. 좋아요 눌렀는지 체크
                    Optional<LikeProduct> getLikeProduct = likeProductRepository.findByProductAndMember(p, p.getMember());
                    boolean isLiked = getLikeProduct.isPresent();

                    return new ProductDto(p, webToken, isLiked);
                })
                .collect(Collectors.toList());

        return productDtoList;
    }

    // 비공개 인증서 공개 인증서로 변환 (온라인에 게시하기[판매하기] 버튼 클릭 시)
    public String updatePublicCertificates(Member member, Long productId, PublicResDto publicResDto) {
        Optional<Product> getProduct = productRepository.findById(productId);
        if(getProduct.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        Product product = getProduct.get();
        product.updatePublicYn(PublicYn.Y);   // 1. 해당 인증서 공개로 변환
        product.updateInfo(publicResDto);     // 2. 가격, 설명 업데이트
        productRepository.save(product);

        return "공개 인증서로 변환 성공";
    }

    // 비공개 인증서 product detail 조회
    public PrivateDetailDto getPrivateProductDetail(Member member, Long productId) {
        Optional<Product> getProduct = productRepository.findById(productId);
        if(getProduct.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        Product product = getProduct.get();

        Long getLikeCount = likeProductRepository.countByProductId(product.getId());
        Optional<LikeProduct> getLikeProduct = likeProductRepository.findByProductAndMember(product, member);
        boolean isLiked = getLikeProduct.isPresent();

        PrivateDetailDto.ProductInfo productInfo = PrivateDetailDto.ProductInfo.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .tags(product.getTags())
                .views(product.getViews())
                .likeCount(getLikeCount)
                .isLiked(isLiked)
                .build();

        Image image = product.getImage();
        PrivateDetailDto.ImageInfo imageInfo = PrivateDetailDto.ImageInfo.builder()
                .imageId(image.getId())
                .url(image.getImageUrl())
                .ownerWebToken(product.getMember().getWebToken())
                .authorshipWebToken(image.getMember().getWebToken())
                .description(product.getDescription())
                .lens(image.getLens())
                .location(image.getLocation())
                .resolution(image.getResolution())
                .size(image.getSize())
                .build();

        List<History> getHistoryList = historyRepository.findAllByProductIdOrderBySoldAtDesc(productId);
        List<PrivateDetailDto.HistoryInfo> historyInfoList = getHistoryList.stream()
                .map(h -> {
                    PrivateDetailDto.HistoryInfo historyInfo = PrivateDetailDto.HistoryInfo.builder()
                            .historyId(h.getId())
                            .sellerWebToken(h.getSeller().getWebToken())
                            .buyerWebToken(h.getBuyer().getWebToken())
                            .price(h.getPrice())
                            .soldAt(h.getSoldAt())
                            .build();

                    return historyInfo;
                })
                .collect(Collectors.toList());

        return new PrivateDetailDto(productInfo, imageInfo, historyInfoList);
    }

    // 공개 인증서 product detail 조회
    public PublicDetailDto getPublicProductDetail(Member member, Long productId) {
        Optional<Product> getProduct = productRepository.findById(productId);
        if(getProduct.isEmpty())
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);

        Product product = getProduct.get();

        Long getLikeCount = likeProductRepository.countByProductId(product.getId());

        PublicDetailDto.ProductInfo productInfo = PublicDetailDto.ProductInfo.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .tags(product.getTags())
                .views(product.getViews())
                .likeCount(getLikeCount)
                .build();

        Image image = product.getImage();
        PublicDetailDto.ImageInfo imageInfo = PublicDetailDto.ImageInfo.builder()
                .imageId(image.getId())
                .url(image.getImageUrl())
                .ownerWebToken(product.getMember().getWebToken())
                .authorshipWebToken(image.getMember().getWebToken())
                .description(product.getDescription())
                .lens(image.getLens())
                .location(image.getLocation())
                .resolution(image.getResolution())
                .size(image.getSize())
                .build();

        List<History> getHistoryList = historyRepository.findAllByProductIdOrderBySoldAtDesc(productId);
        List<PublicDetailDto.HistoryInfo> historyInfoList = getHistoryList.stream()
                .map(h -> {
                    PublicDetailDto.HistoryInfo historyInfo = PublicDetailDto.HistoryInfo.builder()
                            .historyId(h.getId())
                            .sellerWebToken(h.getSeller().getWebToken())
                            .buyerWebToken(h.getBuyer().getWebToken())
                            .price(h.getPrice())
                            .soldAt(h.getSoldAt())
                            .build();

                    return historyInfo;
                })
                .collect(Collectors.toList());

        return new PublicDetailDto(productInfo, imageInfo, historyInfoList);
    }
}
