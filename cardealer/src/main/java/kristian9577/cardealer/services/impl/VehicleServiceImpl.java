package kristian9577.cardealer.services.impl;

import kristian9577.cardealer.data.models.User;
import kristian9577.cardealer.data.models.Vehicle;
import kristian9577.cardealer.data.repository.VehicleRepository;
import kristian9577.cardealer.error.VehicleNotFoundException;
import kristian9577.cardealer.services.LogService;
import kristian9577.cardealer.services.UserService;
import kristian9577.cardealer.services.VehicleService;
import kristian9577.cardealer.services.models.LogServiceModel;
import kristian9577.cardealer.services.models.VehicleAddServiceModel;
import kristian9577.cardealer.validation.vehicle.VehicleValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private ModelMapper modelMapper;
    private VehicleRepository vehicleRepository;
    private UserService userService;
    private VehicleValidator vehicleValidator;
    private LogService logService;

    @Autowired
    public VehicleServiceImpl(ModelMapper modelMapper, VehicleRepository vehicleRepository, UserService userService, VehicleValidator vehicleValidator, LogService logService) {
        this.modelMapper = modelMapper;
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
        this.vehicleValidator = vehicleValidator;
        this.logService = logService;
    }

    @Override
    public VehicleAddServiceModel create(VehicleAddServiceModel vehicleAddServiceModel, String username) {
        if (!this.vehicleValidator.isValid(vehicleAddServiceModel)) {
            throw new RuntimeException("Bad credentials for vehicle!");
        }

        Vehicle vehicle = this.modelMapper.map(vehicleAddServiceModel, Vehicle.class);
        vehicle.setUser(this.modelMapper.map(this.userService.findUserByUserName(username), User.class));
        this.vehicleRepository.saveAndFlush(vehicle);

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(vehicle.getUser().getUsername());
        logServiceModel.setDescription(vehicle.getMaker() + ", " + vehicle.getModel() + " - Add new vehicle");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        return this.modelMapper.map(vehicle, VehicleAddServiceModel.class);
    }

    @Override
    public List<VehicleAddServiceModel> findVehicleByUsername(String name) {
        return this.vehicleRepository.findAllByUser_Username(name)
                .stream()
                .map(event -> modelMapper.map(event, VehicleAddServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleAddServiceModel> findAllVehicles() {
        return this.vehicleRepository.findAll()
                .stream()
                .map(v -> this.modelMapper.map(v, VehicleAddServiceModel.class))
                .collect(Collectors.toList());
    }


    @Override
    public VehicleAddServiceModel findById(String id) {
        return this.vehicleRepository
                .findById(id)
                .map(v -> this.modelMapper.map(v, VehicleAddServiceModel.class))
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with the given id was not found!"));
    }

    @Override
    public void deleteVehicle(String id) {
        Vehicle vehicle = this.vehicleRepository
                .findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with the given id was not found!"));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(vehicle.getUser().getUsername());
        logServiceModel.setDescription(vehicle.getMaker() + ", " + vehicle.getModel() + " - Delete vehicle");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.vehicleRepository.delete(vehicle);
    }

    @Override
    public VehicleAddServiceModel editVehicle(String id, VehicleAddServiceModel serviceModel) {
        Vehicle vehicle = this.vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with the given id was not found!"));

        vehicle.setType(serviceModel.getType());
        vehicle.setModel(serviceModel.getModel());
        vehicle.setMaker(serviceModel.getMaker());
        vehicle.setState(serviceModel.getState());
        vehicle.setYear(serviceModel.getYear());
        vehicle.setMileage(serviceModel.getMileage());
        vehicle.setEuroStandard(serviceModel.getEuroStandard());
        vehicle.setTransmission(serviceModel.getTransmission());
        vehicle.setFuelType(serviceModel.getFuelType());
        vehicle.setColor(serviceModel.getColor());

        if (!this.vehicleValidator.isValid(this.modelMapper.map(vehicle, VehicleAddServiceModel.class))) {
            throw new RuntimeException("Bad credentials for vehicle!");
        }

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(vehicle.getUser().getUsername());
        logServiceModel.setDescription(vehicle.getMaker() + ", " + vehicle.getModel() + " - Edit vehicle");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        return this.modelMapper.map(this.vehicleRepository.saveAndFlush(vehicle), VehicleAddServiceModel.class);
    }
}
