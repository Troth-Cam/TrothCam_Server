package trothly.trothcam.dto.web;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HistoryDto {
    private Long historyId;
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private Long price;
    private LocalDateTime soldAt;
}
