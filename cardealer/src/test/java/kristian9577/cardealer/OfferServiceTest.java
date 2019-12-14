package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.data.models.Offer;
import kristian9577.cardealer.data.repository.OfferRepository;
import kristian9577.cardealer.error.OfferNotFoundException;
import kristian9577.cardealer.services.OfferService;
import kristian9577.cardealer.services.models.OfferServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OfferServiceTest extends BaseTest {
    private List<Offer> list;

    @Autowired
    private OfferService service;
    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private OfferRepository offerRepository;

    @Before
    public void setupTest() {
        list = new ArrayList<>();
        when(offerRepository.findAll())
                .thenReturn(list);
    }

    @Test
    public void findAllEvents_whenNoEvents_returnEmpty() {
        list.clear();
        List<OfferServiceModel> allEvents = service.findAll();
        Assert.assertTrue(allEvents.isEmpty());
    }

    @Test
    public void findOneEvents_whenOneEvents_IdAdd() {
        list.clear();

        Offer offer = new Offer();

        list.add(offer);

        List<OfferServiceModel> results = service.findAll();
        assertEquals(1, results.size());
    }

    @Test
    public void createVehicle_WhenModelIsNull_Throw() {
        OfferServiceModel serviceModer = null;
        assertThrows(Exception.class,
                () -> service.create(serviceModer, "krisko"));
    }

    @Test
    public void createVehicle_WhenUserIsNull_Throw2() {
        OfferServiceModel serviceModer = new OfferServiceModel();
        assertThrows(Exception.class,
                () -> service.create(serviceModer, ""));
    }

    @Test
    public void createVehicle_WhenUserIsNull_Throw22() {
        assertEquals(0, service.findOfferByUsername("").size());
    }

    @Test
    public void findByid_shouldNotFound_Throw() {
        assertThrows(OfferNotFoundException.class,
                () -> service.findById("id"));
    }
    @Test(expected = Exception.class)
    public void deleteEventWithInvalidValue_ThrowError() {
        service.deleteOffer(null);
        verify(offerRepository)
                .save(any());
    }
    @Test(expected = Exception.class)
    public void delete_whenExist_shouldDelete() {
        String id = "1";
        Offer offer = new Offer();
        offer.setId(id);
        Mockito.when(offerRepository
                .findById(id))
                .thenReturn(Optional.of(offer))
                .thenThrow(Exception.class);
        this.service.deleteOffer(id);
        assertThrows(Exception.class,
                () -> this.service.deleteOffer(id));
    }
    @Test(expected = OfferNotFoundException.class)
    public void editOffer_WhenIdNotValid_ShouldThrow() {
        Mockito.when(offerRepository.findById("id"))
                .thenThrow(new OfferNotFoundException());
        OfferServiceModel model = new OfferServiceModel();

        service.editOffer("id", model);
    }

}
