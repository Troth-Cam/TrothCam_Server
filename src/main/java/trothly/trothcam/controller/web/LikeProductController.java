package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.LikeResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.service.web.LikeProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/like-product")
public class LikeProductController {

    private final LikeProductService likeProductService;

    /* 좋아요 저장 */
    @PostMapping("/{productId}")
    public BaseResponse<LikeResDto> saveLike(@PathVariable Long productId, @AuthenticationPrincipal Member member) {
        LikeResDto res = likeProductService.saveLike(productId, member);
        return BaseResponse.onSuccess(res);
    }

    /* 좋아요 삭제 */
    @DeleteMapping("/{productId}")
    public BaseResponse<LikeResDto> deleteLike(@PathVariable Long productId, @AuthenticationPrincipal Member member) {
        LikeResDto res = likeProductService.deleteLike(productId, member);
        return BaseResponse.onSuccess(res);
    }
}
