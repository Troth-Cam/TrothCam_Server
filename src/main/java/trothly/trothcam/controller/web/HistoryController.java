package trothly.trothcam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.HistoryResDto;
import trothly.trothcam.dto.web.TransactionReqDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.service.web.HistoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/transaction")
    public BaseResponse<HistoryResDto> saveHistory(@RequestBody TransactionReqDto req, @AuthenticationPrincipal Member member) {
        if(req.getProductId() == null) {
            throw new BadRequestException("존재하지 않는 상품 아이디 입니다.");
        }

        HistoryResDto res = historyService.saveTransaction(req, member);
        return BaseResponse.onSuccess(res);
    }
}
