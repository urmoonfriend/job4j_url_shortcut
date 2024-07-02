package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.response.StatisticResponse;
import kz.job4j.shortcut.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StatisticControllerTest {

    @Autowired
    private StatisticController controller;

    @MockBean
    private StatisticService statisticService;

    @Test
    public void whenGetStatisticThenOk() {
        List<StatisticResponse> expected =
                List.of(
                        new StatisticResponse().setUrl("someUrl").setCount(2),
                        new StatisticResponse().setUrl("someUrl2").setCount(10)
                );
        when(statisticService.getStatistic()).thenReturn(expected);

        assertThat(controller.getStatistics())
                .isEqualTo(ResponseEntity.ok(ResultMessage.success(expected)));
    }
}
