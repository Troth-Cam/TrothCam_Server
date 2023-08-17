package trothly.trothcam.dto.web.certificate;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicDetailDto {
    @Data
    @Builder
    public static class ProductInfo {
        private Long productId;
        private String title;
        private int tags;
        private int views;
        private Long likeCount;
    }

    @Data
    @Builder
    public static class ImageInfo {
        private Long imageId;
        private String url;
        private String ownerWebToken;   // 소유자 webToken
        private String authorshipWebToken; // 원작자 webToken
        private String description;
        private String lens;
        private String location;
        private String resolution;
        private String size;
    }

    @Data
    @Builder
    public static class HistoryInfo {
        private Long historyId;
        private String sellerWebToken;  // 판매자 webToken
        private String buyerWebToken;   // 구매자 webToken
        private Long price;
        private LocalDateTime soldAt;   // 거래 시간
    }

    private PublicDetailDto.ProductInfo productInfo;
    private PublicDetailDto.ImageInfo imageInfo;
    private List<PublicDetailDto.HistoryInfo> historyInfoList;

    @Builder
    public PublicDetailDto(PublicDetailDto.ProductInfo productInfo, PublicDetailDto.ImageInfo imageInfo, List<PublicDetailDto.HistoryInfo> historyInfoList) {
        this.productInfo = productInfo;
        this.imageInfo = imageInfo;
        this.historyInfoList = historyInfoList;
    }
}
