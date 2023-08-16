package trothly.trothcam.dto.web.certificate;

import lombok.Builder;
import trothly.trothcam.domain.product.Product;

import java.time.LocalDateTime;

public class ProductDto {
    private Long productId;
    private String title;
    private String webToken;
    private Long price;
    private boolean isLiked;
    private LocalDateTime updatedAt;

    @Builder
    public ProductDto(Product product, String webToken, boolean isLiked) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.webToken = webToken;
        this.price = product.getPrice();
        this.isLiked = isLiked;
        this.updatedAt = product.getLastModifiedAt();
    }
}
