package kr.hhplus.be.server.point.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.point.facade.PointFacade;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

    private final PointFacade pointFacade;

    @Operation(summary = "사용자 포인트 조회", description = "사용자의 현재 포인트 잔액을 조회합니다")
    @GetMapping("/{userId}")
    public ApiResponse<PointResponse> getUserPoint(@Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ApiResponse.ok(pointFacade.getUserPoint(userId));
    }

    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다")

    @PostMapping("/{userId}/charge")
    public ApiResponse<PointResponse> chargeUserPoint(@Parameter(description = "사용자 ID") @PathVariable Long userId,
                                                      @RequestBody @Valid PointChargeRequest request) {

        return ApiResponse.ok(pointFacade.chargeUserPoint(userId, request.toFacadeRequest()));
    }
}
