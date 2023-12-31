package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.dto.web.HistoryDto;
import trothly.trothcam.dto.web.HistoryResDto;
import trothly.trothcam.exception.base.BaseException;

import java.util.ArrayList;
import java.util.List;

import static trothly.trothcam.exception.base.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    // 거래 내역 전체 조회
    public List<HistoryDto> findAllHistory(Long productId) {
        List<History> findHistories = historyRepository.findAllByProductIdOrderBySoldAtDesc(productId);
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
    public HistoryResDto saveTransaction(Long productId, Long price, Member member) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BaseException(PRODUCT_IS_NOT_FOUND)
        );

        Member seller = memberRepository.findById(product.getMember().getId()).orElseThrow(
                () -> new BaseException(MEMBER_NOT_FOUND)
        );

        if(member.getId() == product.getMember().getId()) {
            throw new BaseException(SAME_MEMBER);
        }

        History newHistory = historyRepository.save(new History(product, seller, member, price));

        product.updateOwner(member);

        return new HistoryResDto(newHistory.getId(), product.getId(), member.getId(), seller.getId(), price, newHistory.getSoldAt(), member.getId());
    }
}
