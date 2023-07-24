package trothly.trothcam.controller.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.service.ImageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

//    /* 이미지 해시값 저장-촬영 */
//    @PostMapping("")
//    public
//
//    /* 인증 */
//    @GetMapping("/authenticate")
//    public
}
