package trothly.trothcam.controller.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.AWSS3Service;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class AWSS3Controller {
    @Autowired
    AWSS3Service awsS3Service;

    @Value("${cloud.aws.url}")
    private String url;

    // s3에 이미지 업로드 -> 일단 이미지 업로드 되나 확인
    @PostMapping("/image")
    public BaseResponse<String> uploadImageFile(@RequestPart MultipartFile file) throws IOException {

        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            fileUrl = url + awsS3Service.uploadUserFile(file);
        }

        return BaseResponse.onSuccess(fileUrl);
    }

    // s3에 이미지 업로드, 사용자 image 테이블 레코드 추가 ( 웹 공유, url 컬럼)
//    @PostMapping("/image")
//    public BaseResponse<String> uploadImageFile(@RequestPart MultipartFile file, @AuthenticationPrincipal Member member) throws IOException {
//
//        String fileUrl = null;
//        if (file != null && !file.isEmpty()) {
//            fileUrl = url + awsS3Service.uploadUserFile(file);
//        }
//
//        return BaseResponse.onSuccess(res);
//    }


}