package kr.hhplus.be.server.product;

import kr.hhplus.be.server.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.hhplus.be.server.ApiResponseCodeMessage.INVALID_PRODUCT_ID;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductData(@PathVariable Long productId) {
        if (productId < 1) {
            return ApiResponse.badRequest(INVALID_PRODUCT_ID.getCode(), INVALID_PRODUCT_ID.getMessage());
        }
        return ApiResponse.ok(new ProductResponse(productId, "맛좋은 딸기", 12500, 3));
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
