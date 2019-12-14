package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.data.models.Vehicle;
import kristian9577.cardealer.data.repository.VehicleRepository;
import kristian9577.cardealer.error.VehicleNotFoundException;
import kristian9577.cardealer.services.VehicleService;
import kristian9577.cardealer.services.models.VehicleAddServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class VehicleServiceTest extends BaseTest {
    private List<Vehicle> vehicleList;

    @Autowired
    private VehicleService service;
    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private VehicleRepository mockVehicleRepository;

    @Before
    public void setupTest() {
        vehicleList = new ArrayList<>();
        when(mockVehicleRepository.findAll())
                .thenReturn(vehicleList);
    }

    @Test
    public void findAllEvents_whenNoEvents_returnEmpty() {
        vehicleList.clear();
        List<VehicleAddServiceModel> allEvents = service.findAllVehicles();
        Assert.assertTrue(allEvents.isEmpty());
    }

    @Test
    public void findOneEvents_whenOneEvents_IdAdd() {
        vehicleList.clear();

        Vehicle vehicle = new Vehicle();

        vehicleList.add(vehicle);

        List<VehicleAddServiceModel> results = service.findAllVehicles();
        assertEquals(1, results.size());
    }

    @Test
    public void createVehicle_WhenModelIsNull_Throw() {
        VehicleAddServiceModel serviceModer = null;
        assertThrows(Exception.class,
                () -> service.create(serviceModer, "krisko"));
    }

    @Test
    public void createVehicle_WhenUserIsNull_Throw2() {
        VehicleAddServiceModel serviceModer = new VehicleAddServiceModel();
        assertThrows(Exception.class,
                () -> service.create(serviceModer, ""));
    }

    @Test
    public void createVehicle_WhenUserIsNull_Throw22() {
        assertEquals(0, service.findVehicleByUsername("").size());
    }

    @Test
    public void findByid_shouldNotFound_Throw() {
        assertThrows(VehicleNotFoundException.class,
                () -> service.findById("id"));
    }
}
