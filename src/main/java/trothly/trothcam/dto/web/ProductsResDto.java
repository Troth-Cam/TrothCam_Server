package trothly.trothcam.dto.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductsResDto<T> {

    private String title;
    private String ownerWebId;
    private String soldAt;
    private Long price;
    private boolean isLiked;
}
