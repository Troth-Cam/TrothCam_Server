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

import javax.validation.constraints.Positive;
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
    public BaseResponse<String> uploadImageFile(
            @RequestPart MultipartFile multipartFile) throws IOException {

        String fileName = "";
        if(multipartFile != null){ // 파일 업로드한 경우에만

            try{// 파일 업로드
                fileName = awsS3Service.upload(multipartFile, "images"); // S3 버킷의 images 디렉토리 안에 저장됨
                System.out.println("fileName = " + fileName);
            }catch (IOException e){
                return BaseResponse.onSuccess("fail");
            }
        }
        return BaseResponse.onSuccess(fileName);

    }


}