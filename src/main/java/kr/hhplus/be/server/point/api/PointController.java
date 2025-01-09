package kr.hhplus.be.server.point.api;

import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/{userId}")
    public ApiResponse<PointResponse> getUserPoint(@PathVariable Long userId) {
        return ApiResponse.ok(pointFacade.getUserPoint(userId));
    }

    @PostMapping("/{userId}/charge")
    public ApiResponse<PointResponse> chargeUserPoint(@PathVariable Long userId,
                                                      @RequestBody PointChargeRequest request) {

        return ApiResponse.ok(pointFacade.chargeUserPoint(userId, request.toFacadeRequest()));
    }
}
