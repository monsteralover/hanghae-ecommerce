package kr.hhplus.be.server.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.product.service.ProductReadService;
import kr.hhplus.be.server.product.service.dto.ProductMostSoldResponse;
import kr.hhplus.be.server.product.service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductReadService productReadService;

    @Operation(summary = "상품 목록 조회", description = "페이지네이션을 통한 상품 목록을 조회합니다")
    @GetMapping
    public ApiResponse<List<ProductResponse>> getProducts(@Parameter(description = "페이지 번호") @RequestParam int page,
                                                          @Parameter(description = "페이지 크기") @RequestParam int size) {
        return ApiResponse.ok(productReadService.getProducts(page, size));
    }

    @Operation(summary = "베스트 상품 조회", description = "가장 많이 판매된 상품 목록 5개를 조회합니다")
    @GetMapping("/best-products")
    public ApiResponse<List<ProductMostSoldResponse>> getBestProducts() {
        return ApiResponse.ok(productReadService.getMostSoldProducts());
    }


}
