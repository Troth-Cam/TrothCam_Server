package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.ProductReqDto;
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
    @PostMapping("")
    public BaseResponse<LikeResDto> saveLike(@RequestBody ProductReqDto req, @AuthenticationPrincipal Member member) {
        if(req.getProductId() == null) {
            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
        }

        LikeResDto res = likeProductService.saveLike(req, member);
        return BaseResponse.onSuccess(res);
    }

    /* 좋아요 삭제 */
    @DeleteMapping("")
    public BaseResponse<LikeResDto> deleteLike(@RequestBody ProductReqDto req, @AuthenticationPrincipal Member member) {
        if(req.getProductId() == null) {
            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
        }

        LikeResDto res = likeProductService.deleteLike(req, member);
        return BaseResponse.onSuccess(res);
    }
}
