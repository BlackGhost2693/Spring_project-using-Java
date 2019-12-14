package kristian9577.cardealer.services;

import kristian9577.cardealer.services.models.VehicleAddServiceModel;

import java.util.List;

public interface VehicleService {
    VehicleAddServiceModel create(VehicleAddServiceModel vehicleAddServiceModel, String username);

    List<VehicleAddServiceModel> findVehicleByUsername(String name);

    List<VehicleAddServiceModel> findAllVehicles();

    VehicleAddServiceModel findById(String id);

    void deleteVehicle(String id);

    VehicleAddServiceModel editVehicle(String id, VehicleAddServiceModel map);
}
