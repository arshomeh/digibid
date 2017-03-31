package gr.uoa.di.digibid.service;

import gr.uoa.di.digibid.model.WebCountry;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface CountryService {

    Optional<WebCountry> getCountryByName(String countryName);

    List<WebCountry> getAllCountries();

    List<WebCountry> search(String countryName);

    WebCountry createOrUpdate(WebCountry webCountry);
}
