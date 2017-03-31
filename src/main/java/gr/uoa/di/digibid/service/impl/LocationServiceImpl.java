package gr.uoa.di.digibid.service.impl;

import gr.uoa.di.digibid.model.WebLocation;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.domain.Location;
import gr.uoa.di.digibid.persist.repository.LocationRepository;
import gr.uoa.di.digibid.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Optional<WebLocation> getLocationByNameAndByLatLon(String locationName, String latitude, String longitude) {
        Optional<Location> location = locationRepository.findByNameAndByLatLon(locationName, latitude, longitude);
        return location.isPresent() ? Optional.ofNullable(DomainConverter.convert(location.get())) : Optional.empty();
    }

    @Override
    public List<WebLocation> getAllLocations() {
        return locationRepository.findAll(new Sort("name")).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public WebLocation createOrUpdate(WebLocation webLocation) {
        return DomainConverter.convert(locationRepository.saveAndFlush(DomainConverter.convert(webLocation)));
    }
}
