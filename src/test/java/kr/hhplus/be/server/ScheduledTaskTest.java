package kr.hhplus.be.server;

import kr.hhplus.be.server.product.service.ProductStockCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskTest {

    @Mock
    private ProductStockCommandService productStockCommandService;
    @InjectMocks
    private ScheduledTask scheduledTask;


    @DisplayName("스케쥴러가 3일 자정에 한 번씩 불리는 것을 검증한다.")
    @Test
    void validateCronExpression() {
        // Given
        String cronExpression = "0 0 0 */3 * *";
        CronTrigger trigger = new CronTrigger(cronExpression);

        LocalDateTime currentTime = LocalDateTime.of(2024, 1, 9, 0, 0, 0);
        Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        // When
        List<LocalDateTime> nextExecutions = new ArrayList<>();
        Date nextExecutionTime = currentDate;
        SimpleTriggerContext triggerContext = new SimpleTriggerContext();

        for (int i = 0; i < 5; i++) {
            triggerContext.update(nextExecutionTime, nextExecutionTime, nextExecutionTime);
            nextExecutionTime = trigger.nextExecutionTime(triggerContext);

            LocalDateTime nextExecution = LocalDateTime.ofInstant(
                    nextExecutionTime.toInstant(),
                    ZoneId.systemDefault()
            );
            nextExecutions.add(nextExecution);
        }

        // Then
        assertThat(nextExecutions)
                .hasSize(5)
                .containsExactly(
                        LocalDateTime.of(2024, 1, 10, 0, 0, 0),
                        LocalDateTime.of(2024, 1, 13, 0, 0, 0),
                        LocalDateTime.of(2024, 1, 16, 0, 0, 0),
                        LocalDateTime.of(2024, 1, 19, 0, 0, 0),
                        LocalDateTime.of(2024, 1, 22, 0, 0, 0)
                );
    }

    @DisplayName("scheduledTask의 resetAccumulatedSoldCountEveryThreeDays() 호출 시 productStockCommandService" +
            ".resetAccumulatedSoldCountEveryThreeDays()가 호출된다. ")
    @Test
    void testTheServiceCalled() {
        // When
        scheduledTask.resetAccumulatedSoldCountEveryThreeDays();

        // Then
        verify(productStockCommandService, times(1))
                .resetAccumulatedSoldCountEveryThreeDays();
    }

}
