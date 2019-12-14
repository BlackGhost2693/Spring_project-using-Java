package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.services.models.VehicleAddServiceModel;
import kristian9577.cardealer.validation.vehicle.VehicleValidator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleValidationTest extends BaseTest {

    @Autowired
    VehicleValidator validator;

    @Test
    public void isValid_whenEverythingIsOK_shouldReturnTrue() {
        VehicleAddServiceModel serviceModel = new VehicleAddServiceModel();
        serviceModel.setType("Car");
        serviceModel.setMaker("Maker");
        serviceModel.setModel("Model");
        serviceModel.setState("New");
        serviceModel.setYear(1);
        serviceModel.setMileage(1);
        serviceModel.setEuroStandard("EURO1");
        serviceModel.setColor("Red");
        serviceModel.setFuelType("Diesel");
        serviceModel.setTransmission("Auto");
        serviceModel.setId("ID");

        boolean isValid = validator.isValid(serviceModel);
        Assert.assertTrue(isValid);
    }

    @Test
    public void isValid_whenMakerIsEmpty_shouldReturnTrue() {
        VehicleAddServiceModel serviceModel = new VehicleAddServiceModel();
        serviceModel.setType("Car");
        serviceModel.setMaker("");
        serviceModel.setModel("Model");
        serviceModel.setState("New");
        serviceModel.setYear(1);
        serviceModel.setMileage(1);
        serviceModel.setEuroStandard("EURO1");
        serviceModel.setColor("Red");
        serviceModel.setFuelType("Diesel");
        serviceModel.setTransmission("Auto");
        serviceModel.setId("ID");

        boolean isValid = validator.isValid(serviceModel);
        Assert.assertFalse(isValid);
    }

    @Test
    public void isValid_whenModelIsEmpty_shouldReturnTrue() {
        VehicleAddServiceModel serviceModel = new VehicleAddServiceModel();
        serviceModel.setType("Car");
        serviceModel.setMaker("Maker");
        serviceModel.setModel("");
        serviceModel.setState("New");
        serviceModel.setYear(1);
        serviceModel.setMileage(1);
        serviceModel.setEuroStandard("EURO1");
        serviceModel.setColor("Red");
        serviceModel.setFuelType("Diesel");
        serviceModel.setTransmission("Auto");
        serviceModel.setId("ID");

        boolean isValid = validator.isValid(serviceModel);
        Assert.assertFalse(isValid);
    }
    @Test
    public void isValid_whenYearIsZero_shouldReturnTrue() {
        VehicleAddServiceModel serviceModel = new VehicleAddServiceModel();
        serviceModel.setType("Car");
        serviceModel.setMaker("");
        serviceModel.setModel("Model");
        serviceModel.setState("New");
        serviceModel.setYear(0);
        serviceModel.setMileage(1);
        serviceModel.setEuroStandard("EURO1");
        serviceModel.setColor("Red");
        serviceModel.setFuelType("Diesel");
        serviceModel.setTransmission("Auto");
        serviceModel.setId("ID");

        boolean isValid = validator.isValid(serviceModel);
        Assert.assertFalse(isValid);
    }
    @Test
    public void isValid_whenMileageIsZero_shouldReturnTrue() {
        VehicleAddServiceModel serviceModel = new VehicleAddServiceModel();
        serviceModel.setType("Car");
        serviceModel.setMaker("");
        serviceModel.setModel("Model");
        serviceModel.setState("New");
        serviceModel.setYear(1);
        serviceModel.setMileage(0);
        serviceModel.setEuroStandard("EURO1");
        serviceModel.setColor("Red");
        serviceModel.setFuelType("Diesel");
        serviceModel.setTransmission("Auto");
        serviceModel.setId("ID");

        boolean isValid = validator.isValid(serviceModel);
        Assert.assertFalse(isValid);
    }
}