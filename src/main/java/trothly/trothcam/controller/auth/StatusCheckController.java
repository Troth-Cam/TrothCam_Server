package trothly.trothcam.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusCheckController {

    // 로드 밸런싱 8080 포트 health-check를 위한 API
    @GetMapping("/health-check")
    public ResponseEntity<Void> checkHealthStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
