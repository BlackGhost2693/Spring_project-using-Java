package kristian9577.cardealer.services.impl;

import kristian9577.cardealer.data.models.Offer;
import kristian9577.cardealer.data.models.User;
import kristian9577.cardealer.data.models.Vehicle;
import kristian9577.cardealer.data.repository.OfferRepository;
import kristian9577.cardealer.error.OfferNotFoundException;
import kristian9577.cardealer.error.OfferWithThisVehicleAlreadyExistsException;
import kristian9577.cardealer.services.LogService;
import kristian9577.cardealer.services.OfferService;
import kristian9577.cardealer.services.UserService;
import kristian9577.cardealer.services.VehicleService;
import kristian9577.cardealer.services.models.LogServiceModel;
import kristian9577.cardealer.services.models.OfferServiceModel;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private VehicleService vehicleService;
    private OfferRepository offerRepository;
    private LogService logService;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(VehicleService vehicleService, OfferRepository offerRepository, LogService logService, UserService userService, ModelMapper modelMapper) {
        this.vehicleService = vehicleService;
        this.offerRepository = offerRepository;
        this.logService = logService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(OfferServiceModel offerServiceModel, String username) {
        Offer offer = this.modelMapper.map(offerServiceModel, Offer.class);
        Vehicle vehicle = modelMapper.map(this.vehicleService.findById(offerServiceModel.getVehicle().getId()), Vehicle.class);

        if (offer.getVehicle().getOffer() != null) {
            throw new OfferWithThisVehicleAlreadyExistsException("Offer with this vehicle already exists");
        }
        vehicle.setUser(this.modelMapper.map(this.userService.findUserByUserName(username), User.class));


        offer.setUser(this.modelMapper.map(this.userService.findUserByUserName(username), User.class));
        offer.setVehicle(vehicle);

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(offer.getUser().getUsername());
        logServiceModel.setDescription(vehicle.getMaker() + ", " + vehicle.getModel() +
                ", " + offer.getCreatedOn() + " - Add offer");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.offerRepository.saveAndFlush(offer);
    }

    @Override
    public List<OfferServiceModel> findAll() {
        return this.offerRepository.findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferServiceModel> findOfferByUsername(String username) {
        return this.offerRepository.findAllByUser_Username(username)
                .stream()
                .map(event -> modelMapper.map(event, OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OfferServiceModel findById(String id) {
        return this.offerRepository
                .findById(id)
                .map(o -> this.modelMapper.map(o, OfferServiceModel.class))
                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));
    }

    @Override
    public void deleteOffer(String id) {
        Offer offer = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(offer.getUser().getUsername());
        logServiceModel.setDescription(offer.getVehicle().getMaker() + ", " + offer.getCreatedOn() + " - Delete offer");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.offerRepository.delete(offer);
    }

    @Override
    public OfferServiceModel editOffer(String id, OfferServiceModel offerServiceModel) {
        Offer offer = this.offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));
        offer.setCreatedOn(offerServiceModel.getCreatedOn());
        offer.setValidUntil(offerServiceModel.getValidUntil());
        offer.setPrice(offerServiceModel.getPrice());
        offer.setDescription(offerServiceModel.getDescription());


        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(offer.getUser().getUsername());
        logServiceModel.setDescription(offer.getVehicle().getMaker() + ", " + offer.getCreatedOn() + " - Edit offer");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        return this.modelMapper.map(this.offerRepository.saveAndFlush(offer), OfferServiceModel.class);
    }

    @Scheduled(fixedRate = 5000000)
    private void deleteOfferIfDateIsWrong() {
        List<Offer> offers = new ArrayList<>(this.offerRepository.findAll());
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateTime nowDate = DateTime.parse(now);

        for (Offer offer : offers) {
            DateTime createdOn = DateTime.parse(offer.getCreatedOn());
            DateTime validUntil = DateTime.parse(offer.getValidUntil());
            if (createdOn.isAfter(validUntil) || validUntil.isBefore(nowDate)) {
                this.offerRepository.delete(offer);
            }
        }
    }
}
