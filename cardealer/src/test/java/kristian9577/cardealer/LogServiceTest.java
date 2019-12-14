package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.data.models.Log;
import kristian9577.cardealer.services.LogService;
import kristian9577.cardealer.services.models.LogServiceModel;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogServiceTest extends BaseTest {

    @Autowired
    private LogService service;
    @Autowired
    private ModelMapper modelMapper;


    @Test
    public void createLog_WhenItIsOk() {
        String str = "2016-03-04 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        Log log = new Log();
        log.setUsername("Peshko");
        log.setTime(dateTime);
        log.setDescription("ddd");

        LogServiceModel model = service.seedLogInDB(modelMapper.map(log, LogServiceModel.class));

        assertEquals("Peshko", model.getUsername());
        assertEquals(dateTime, model.getTime());
        assertEquals("ddd", model.getDescription());


    }
}
