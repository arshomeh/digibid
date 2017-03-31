package gr.uoa.di.digibid.service.impl;

import gr.uoa.di.digibid.model.WebCountry;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.domain.Country;
import gr.uoa.di.digibid.persist.repository.CountryRepository;
import gr.uoa.di.digibid.service.CountryService;
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
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Optional<WebCountry> getCountryByName(String countryName) {
        Optional<Country> country = countryRepository.findByName(countryName);
        return country.isPresent() ? Optional.ofNullable(DomainConverter.convert(country.get())) : Optional.empty();
    }

    @Override
    public List<WebCountry> getAllCountries() {
        return countryRepository.findAll(new Sort("name")).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public List<WebCountry> search(String countryName) {
        return countryRepository.findByNameContaining(new Sort("name"), countryName).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public WebCountry createOrUpdate(WebCountry webCountry) {
        return DomainConverter.convert(countryRepository.saveAndFlush(DomainConverter.convert(webCountry)));
    }
}
