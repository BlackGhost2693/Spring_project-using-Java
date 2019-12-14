package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.data.models.Event;
import kristian9577.cardealer.data.repository.EventRepository;
import kristian9577.cardealer.error.EventNameAlreadyExistsException;
import kristian9577.cardealer.error.EventNotFoundException;
import kristian9577.cardealer.services.EventService;
import kristian9577.cardealer.services.models.EventAddServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class EventServiceTest extends BaseTest {

    private List<Event> events;

    @Autowired
    private EventService service;
    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private EventRepository mockEventRepository;

    @Before
    public void setupTest() {
        events = new ArrayList<>();
        when(mockEventRepository.findAll())
                .thenReturn(events);
    }

    @Test
    public void createEvent_WhenEventAlreadyExist_shouldThrow() {

        Event event = new Event();
        event.setName("Peshko");
        when(mockEventRepository.findByName(event.getName()))
                .thenReturn(Optional.of(event));
        assertThrows(
                EventNameAlreadyExistsException.class,
                () -> service.createEvent(modelMapper.map(event, EventAddServiceModel.class), "krisko"));
    }

    @Test
    public void createEvent_WhenItIsOk() {

        Event event = new Event();
        event.setName("Peshko");
        event.setDate("2900-12-12");
        event.setDescription("ddd");
        event.setImageUrl("imgUrl");

        EventAddServiceModel model = service.createEvent(modelMapper.map(event, EventAddServiceModel.class), "krisko");

        assertEquals("Peshko", model.getName());
        assertEquals("2900-12-12", model.getDate());
        assertEquals("ddd", model.getDescription());
        assertEquals("imgUrl", model.getImageUrl());

    }


    @Test
    public void findAllEvents_whenNoEvents_returnEmpty() {
        events.clear();
        List<EventAddServiceModel> allEvents = service.findAllEvents();
        Assert.assertTrue(allEvents.isEmpty());
    }

    @Test
    public void findOneEvents_whenOneEvents_IdAdd() {
        events.clear();

        Event event = new Event();
        event.setName("Peshko");
        event.setDate("2900-12-12");
        event.setDescription("ddd");
        event.setImageUrl("imgUrl");

        events.add(event);

        List<EventAddServiceModel> results = service.findAllEvents();
        assertEquals(1, results.size());
    }

    @Test
    public void createEvents_WhenModelIsNull_Throw() {

        EventAddServiceModel serviceModer = null;
        assertThrows(NullPointerException.class,
                () -> service.createEvent(serviceModer, "krisko"));

    }

    @Test
    public void createEvents_WhenUserIsNull_Throws() {

        EventAddServiceModel serviceModer = new EventAddServiceModel();
        serviceModer.setName("Peshko");
        serviceModer.setDate("2900-12-12");
        serviceModer.setDescription("ddd");
        serviceModer.setImageUrl("imgUrl");
        assertThrows(UsernameNotFoundException.class,
                () -> service.createEvent(serviceModer, "null"));

    }

    @Test
    public void findEvents_whenGivenIdIsNotCorrect_throw() {
        Event event = new Event();
        event.setName("Peshko");
        when(mockEventRepository.findById(event.getName()))
                .thenReturn(Optional.of(event));
        assertThrows(
                EventNotFoundException.class,
                () -> service.findById("krisko"));

    }

    @Test(expected = Exception.class)
    public void deleteEventWithInvalidValue_ThrowError() {
        service.deleteEvent(null);
        verify(mockEventRepository)
                .save(any());
    }

    @Test(expected = Exception.class)
    public void delete_whenExist_shouldDelete() {
        String id = "1";
        Event event = new Event();
        event.setId(id);
        Mockito.when(mockEventRepository
                .findById(id))
                .thenReturn(Optional.of(event))
                .thenThrow(Exception.class);
        this.service.deleteEvent(id);
        assertThrows(Exception.class,
                () -> this.service.deleteEvent(id));
    }

    @Test(expected = EventNotFoundException.class)
    public void editOffer_WhenIdNotValid_ShouldThrow() {
        Mockito.when(mockEventRepository.findById("id"))
                .thenThrow(new EventNotFoundException());
        EventAddServiceModel model = new EventAddServiceModel();

        service.editEvent("id", model);
    }
}
