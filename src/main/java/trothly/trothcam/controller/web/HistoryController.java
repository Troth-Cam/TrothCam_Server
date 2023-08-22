package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.HistoryResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.web.HistoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/transaction/{productId}/{price}")
    public BaseResponse<HistoryResDto> saveHistory(@PathVariable Long productId, @PathVariable Long price, @AuthenticationPrincipal Member member) {
        HistoryResDto res = historyService.saveTransaction(productId, price, member);
        return BaseResponse.onSuccess(res);
    }
}
