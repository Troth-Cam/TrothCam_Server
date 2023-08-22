package trothly.trothcam.dto.web.certificate;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateDetailDto {
    @Data
    @Builder
    public static class ProductInfo {
        private Long productId;
        private String title;
        private int tags;
        private int views;
        private Long likeCount;
        private boolean isLiked;
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

    private ProductInfo productInfo;
    private ImageInfo imageInfo;
    private List<HistoryInfo> historyInfoList;

    @Builder
    public PrivateDetailDto(ProductInfo productInfo, ImageInfo imageInfo, List<HistoryInfo> historyInfoList) {
        this.productInfo = productInfo;
        this.imageInfo = imageInfo;
        this.historyInfoList = historyInfoList;
    }
}
