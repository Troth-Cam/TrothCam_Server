package trothly.trothcam.dto.web.certificate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicResDto {
    private Long price;
    private String description;
}
