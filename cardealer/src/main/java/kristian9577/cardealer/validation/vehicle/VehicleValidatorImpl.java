package kristian9577.cardealer.validation.vehicle;

import kristian9577.cardealer.services.models.VehicleAddServiceModel;
import org.springframework.stereotype.Service;

@Service
public class VehicleValidatorImpl implements VehicleValidator {
    @Override
    public boolean isValid(VehicleAddServiceModel serviceModel) {
        return serviceModel != null &&
                serviceModel.getMaker().length() > 0 &&
                serviceModel.getModel().length() > 0 &&
                serviceModel.getYear() > 0 &&
                serviceModel.getMileage() > 0;
    }
}
