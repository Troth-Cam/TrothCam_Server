package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.certificate.PrivateDetailDto;
import trothly.trothcam.dto.web.certificate.ProductDto;
import trothly.trothcam.dto.web.certificate.PublicDetailDto;
import trothly.trothcam.dto.web.certificate.PublicResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.web.CertificateService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class CertificateController {

    private final CertificateService certificateService;

    // 공개 인증서 비공개 인증서로 변환 (비공개하기[판매취소] 클릭 시)
    @PatchMapping("/{productId}/convert-to-private")
    public BaseResponse<List<ProductDto>> getPrivateCertificates(@AuthenticationPrincipal Member member, @PathVariable Long productId) {
        List<ProductDto> getPrivateCertificates = certificateService.getPrivateCertificates(member, productId);
        return BaseResponse.onSuccess(getPrivateCertificates);
    }

    // 비공개 인증서 공개 인증서로 변환 (온라인에 게시하기[판매하기] 버튼 클릭 시)
    @PatchMapping("/{productId}/convert-to-public")
    public BaseResponse<String> updatePublicCertificates(@AuthenticationPrincipal Member member, @PathVariable Long productId, @RequestBody PublicResDto publicResDto) {
        String result = certificateService.updatePublicCertificates(member, productId, publicResDto);
        return BaseResponse.onSuccess(result);
    }

    // 비공개 인증서 product detail 조회
    @GetMapping("/private/{productId}")
    public BaseResponse<PrivateDetailDto> getPrivateProductDetail(@AuthenticationPrincipal Member member, @PathVariable Long productId) {
        PrivateDetailDto getPrivateDetailDto = certificateService.getPrivateProductDetail(member, productId);
        return BaseResponse.onSuccess(getPrivateDetailDto);
    }

    // 공개 인증서 product detail 조회
    @GetMapping("/public/{productId}")
    public BaseResponse<PublicDetailDto> getPublicProductDetail(@AuthenticationPrincipal Member member, @PathVariable Long productId) {
        PublicDetailDto getPublicProductDetail = certificateService.getPublicProductDetail(member, productId);
        return BaseResponse.onSuccess(getPublicProductDetail);
    }
}
