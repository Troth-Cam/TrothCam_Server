package trothly.trothcam.dto.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductsResDto<T> {

    private Long productId;
    private String title;
    private String ownerWebId;
    private LocalDateTime soldAt;
    private Long price;
    private boolean isLiked;
}
