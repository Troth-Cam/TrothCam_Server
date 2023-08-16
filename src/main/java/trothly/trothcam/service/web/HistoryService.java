package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.dto.web.HistoryDto;
import trothly.trothcam.dto.web.ProductReqDto;
import trothly.trothcam.exception.base.BaseException;

import java.util.ArrayList;
import java.util.List;

import static trothly.trothcam.exception.base.ErrorCode.HISTORIES_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ProductRepository productRepository;

    // 거래 내역 전체 조회
    public List<HistoryDto> findAllHistory(ProductReqDto req) {
        List<History> findHistories = historyRepository.findAllByProductId(req.getProductId());
//        if(findHistories == null || findHistories.isEmpty()) {
//            throw new BaseException(HISTORIES_NOT_FOUND);
//        }

        List<HistoryDto> historyDto = new ArrayList<>();

        for (History history : findHistories) {
            HistoryDto historyOne = new HistoryDto(history.getId(), history.getProduct().getId(), history.getSeller().getId(), history.getBuyer().getId(), history.getPrice(), history.getSoldAt());
            historyDto.add(historyOne);
        }

        return historyDto;
    }

    // 거래 내역 저장
}
