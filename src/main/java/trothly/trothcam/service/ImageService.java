package trothly.trothcam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.image.ImageRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.app.CheckImgHashResDto;
import trothly.trothcam.dto.app.ImgHashReqDto;
import trothly.trothcam.dto.app.SaveImgHashResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.custom.BadRequestException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;

    // 이미지 해시 값 받아서 저장
    public SaveImgHashResDto saveImgHash(ImgHashReqDto req, Member member) throws BaseException {
        Optional<Image> imgHash = imageRepository.findByImageHash(req.getImageHash());

        if(imgHash.isPresent()){
            throw new BadRequestException("이미 존재하는 해시 값입니다.");
        }

        Image image = imageRepository.save(new Image(req.getImageHash(), member));

        return new SaveImgHashResDto(image.getId());
    }

    // 이미지 해시 값 비교해서 리턴
    public CheckImgHashResDto checkImgHash(ImgHashReqDto req) throws BaseException {
        Optional<Image> imgHash = imageRepository.findByImageHash(req.getImageHash());
        if(imgHash.isEmpty()){
            throw new BadRequestException("존재하지 않는 해시 값입니다.");
        }
        return new CheckImgHashResDto("인증된 해시값입니다.");
    }

    //이미지 찾기
    public Optional<Image> findImage(Long id) throws BaseException{
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty()) {
            throw new BadRequestException("이미지가 존재하지 않습니다.");
        }
        return image;
    }
}
