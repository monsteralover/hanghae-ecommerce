package kr.hhplus.be.server.product.controller;

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

    @GetMapping
    public ApiResponse<List<ProductResponse>> getProducts(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.ok(productReadService.getProducts(page, size));
    }

    @GetMapping("/best-products")
    public ApiResponse<List<ProductMostSoldResponse>> getBestProducts() {
        return ApiResponse.ok(productReadService.getMostSoldProducts());
    }


}
