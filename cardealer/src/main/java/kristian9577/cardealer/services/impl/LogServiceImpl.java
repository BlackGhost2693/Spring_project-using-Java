package kristian9577.cardealer.services.impl;

import kristian9577.cardealer.data.models.Log;
import kristian9577.cardealer.data.repository.LogRepository;
import kristian9577.cardealer.services.LogService;
import kristian9577.cardealer.services.models.LogServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public LogServiceModel seedLogInDB(LogServiceModel logServiceModel) {
        Log log = this.modelMapper.map(logServiceModel, Log.class);
        return this.modelMapper.map(this.logRepository.saveAndFlush(log),LogServiceModel.class);
    }
}
