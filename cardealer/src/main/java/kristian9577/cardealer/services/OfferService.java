package kristian9577.cardealer.services;

import kristian9577.cardealer.services.models.OfferServiceModel;

import java.util.List;

public interface OfferService {

    void create(OfferServiceModel offerServiceModel, String username);

    List<OfferServiceModel> findAll();

    List<OfferServiceModel> findOfferByUsername(String username);

    OfferServiceModel findById(String id);

    void deleteOffer(String id);

    OfferServiceModel editOffer(String id, OfferServiceModel offerServiceModel);
}
