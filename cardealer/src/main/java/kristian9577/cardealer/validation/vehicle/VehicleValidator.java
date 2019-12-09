package kristian9577.cardealer.validation.vehicle;

import kristian9577.cardealer.services.models.VehicleAddServiceModel;

public interface VehicleValidator {
    boolean isValid(VehicleAddServiceModel serviceModel);
}