package trothly.trothcam.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.history.History;
import trothly.trothcam.domain.history.HistoryRepository;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.like.LikeProduct;
import trothly.trothcam.domain.like.LikeProductRepository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;
import trothly.trothcam.domain.product.ProductRepository;
import trothly.trothcam.dto.app.CheckImgHashResDto;
import trothly.trothcam.dto.auth.web.CheckIdResDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenResDto;
import trothly.trothcam.dto.web.ProductDetailResDto;
import trothly.trothcam.dto.web.ProductReqDto;
import trothly.trothcam.dto.web.ProductsResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.exception.custom.ProductNotFoundException;
import trothly.trothcam.exception.custom.SignupException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

//
//    /* 공개 인증서 조회 */
//    @Transactional(readOnly = true)
//    public List<Product> findPublicProducts(String webId) throws BaseException {
//        List<Product> findProducts = productRepository.findAllByMember_WebIdAndPublicYn_Y(webId);
//        if (findProducts == null || findProducts.isEmpty())
//            throw new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
//
//        return findProducts;
//    }

}
