package gr.uoa.di.digibid.service;

import java.util.List;
import java.util.Optional;

import gr.uoa.di.digibid.model.WebLocation;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface LocationService {

    Optional<WebLocation> getLocationByNameAndByLatLon(String locationName, String latitude, String longitude);

    List<WebLocation> getAllLocations();

    WebLocation createOrUpdate(WebLocation webLocation);
}
