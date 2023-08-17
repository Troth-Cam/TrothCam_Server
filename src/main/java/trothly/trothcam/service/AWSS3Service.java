package trothly.trothcam.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file, int userIdx) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());

        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            InputStream inputStream = file.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(AmazonServiceException e){
            e.printStackTrace();
        } catch(SdkClientException e){
            e.printStackTrace();
        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        // S3 이미지 서버에 등록한 파일명을 반환
        return fileName;
    }

    public String uploadUserFile(MultipartFile file) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());

        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            InputStream inputStream = file.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            System.out.println(inputStream);
        } catch(AmazonServiceException e){
            e.printStackTrace();
        } catch(SdkClientException e){
            e.printStackTrace();
        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        // S3 이미지 서버에 등록한 파일명을 반환
        return fileName;
    }

    public String uploadAdFile(MultipartFile file) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());

        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            InputStream inputStream = file.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(AmazonServiceException e){
            e.printStackTrace();
        } catch(SdkClientException e){
            e.printStackTrace();
        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        // S3 이미지 서버에 등록한 파일명을 반환
        return fileName;
    }


    public boolean deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        return true;
    }

    // 기존 확장자명을 유지하면서, 식별되는 파일명을 생성
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 확장자 알아내기
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    public String uploadChatFile(MultipartFile file) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());

        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            InputStream inputStream = file.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(AmazonServiceException e){
            e.printStackTrace();
        } catch(SdkClientException e){
            e.printStackTrace();
        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
        // S3 이미지 서버에 등록한 파일명을 반환
        return fileName;
    }


}
