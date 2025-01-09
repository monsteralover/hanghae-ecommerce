package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.product.service.ProductReadService;
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

    @GetMapping()
    public ApiResponse<List<ProductResponse>> getProducts(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.ok(productReadService.getProducts(page, size));
    }

    @GetMapping("/best-products")
    public ApiResponse<List<ProductResponse>> getBestProducts() {

        return ApiResponse.ok(
                List.of(
                        new ProductResponse(1L, "맛좋은 딸기", 12500, 3),
                        new ProductResponse(2L, "맛좋은 바나나", 3500, 31),
                        new ProductResponse(3L, "맛좋은 블루베리", 4500, 34)
                ));
    }


}
