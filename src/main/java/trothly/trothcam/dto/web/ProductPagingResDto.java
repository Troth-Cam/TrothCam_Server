package trothly.trothcam.dto.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductPagingResDto {
    private Long historyId;
    private Long productId;
    private String ownerWebId;
    private String ownerToken;
    private String ownerName;
    private String authorshipWebId;
    private String authorshipToken;
    private String title;
    private int tags;
    private String imageUrl;
    private Long price;
    private LocalDateTime soldAt;
    private boolean liked;
}
