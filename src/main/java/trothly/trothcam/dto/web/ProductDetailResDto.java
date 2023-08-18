package trothly.trothcam.dto.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.product.PublicYn;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDetailResDto {

    private Long productId;
    private Long imageId;
    private String ownerToken;
    private String authorshipToken;
    private String title;
    private int tags;
    private Long price;
    private String description;
    private int views;
    private Long likes;
    private PublicYn publicYN;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean liked;
    private List<HistoryDto> histories;

}
