package trothly.trothcam.dto.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductsPagingLikedResDto {
    private List<ProductPagingResDto> getProductPagingResDto;
    private int totalPages;
}
