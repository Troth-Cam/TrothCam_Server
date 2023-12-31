package trothly.trothcam.controller.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.app.CheckImgHashResDto;
import trothly.trothcam.dto.app.ImgHashReqDto;
import trothly.trothcam.dto.app.SaveImgHashResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.custom.*;
import trothly.trothcam.service.ImageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    /* 이미지 해시값 저장-촬영 */
    @PostMapping("")
    public BaseResponse<SaveImgHashResDto> saveImageHash(@RequestBody ImgHashReqDto req, @AuthenticationPrincipal Member member) {
        if(req.getImageHash() == null) {
            throw new BadRequestException("해시 값을 찾을 수 없습니다");
        }

        SaveImgHashResDto res = imageService.saveImgHash(req, member);
        return BaseResponse.onSuccess(res);
    }

    /* 인증 (swift에서 Alamofire 사용 시 get방식에서 json으로 request body 받을 수 없음) */
    @PostMapping("/authenticate")
    public BaseResponse<CheckImgHashResDto> checkImgHash(@RequestBody ImgHashReqDto req) {
        if(req.getImageHash() == null) {
            throw new BadRequestException("해시 값을 찾을 수 없습니다.");
        }

        CheckImgHashResDto res = imageService.checkImgHash(req);
        return BaseResponse.onSuccess(res);
    }
}
