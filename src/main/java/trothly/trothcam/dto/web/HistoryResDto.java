package trothly.trothcam.dto.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HistoryResDto {
    private Long historyId;
    private Long productId;
    private Long buyerId;
    private Long sellerId;
    private Long price;
    private LocalDateTime soldAt;
    private Long newOwnerId;
}
