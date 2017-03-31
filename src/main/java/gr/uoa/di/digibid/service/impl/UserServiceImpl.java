package gr.uoa.di.digibid.service.impl;

import gr.uoa.di.digibid.model.WebCountry;
import gr.uoa.di.digibid.model.WebLocation;
import gr.uoa.di.digibid.model.WebUser;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import gr.uoa.di.digibid.persist.domain.User;
import gr.uoa.di.digibid.persist.repository.UserRepository;
import gr.uoa.di.digibid.service.CountryService;
import gr.uoa.di.digibid.service.LocationService;
import gr.uoa.di.digibid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private CountryService countryService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<WebUser> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? Optional.ofNullable(DomainConverter.convert(user.get())) : Optional.empty();
    }

    @Override
    public Optional<WebUser> getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() ? Optional.ofNullable(DomainConverter.convert(user.get())) : Optional.empty();
    }

    @Override
    public List<WebUser> getAllUsers() {
        return userRepository.findAll().stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public WebUser createOrUpdate(WebUser webUser) {

        WebCountry webCountry = countryService.getCountryByName(webUser.getCountryName()).orElseGet(() -> WebCountry.builder().build());

        if (webCountry.getId() == null) {
            webCountry = countryService.createOrUpdate(WebCountry.builder().name(webUser.getCountryName()).build());
        }

        WebLocation webLocation = locationService.getLocationByNameAndByLatLon(webUser.getLocationName(), null, null).orElseGet(() -> WebLocation.builder().build());

        if (webLocation.getId() == null) {
            webLocation = locationService.createOrUpdate(WebLocation.builder().name(webUser.getLocationName()).country(webCountry).build());
        }

        webUser.setCreationDate(LocalDateTimeConverter.toFormattedString(LocalDateTime.now()));
        webUser.setLocation(webLocation);
        webUser.setCountry(webCountry);

        User user = DomainConverter.convert(webUser);
        user.setPassword(new BCryptPasswordEncoder().encode(webUser.getPassword()));

        LOGGER.debug("Created/updated new user={}", user);

        return DomainConverter.convert(userRepository.saveAndFlush(user));
    }

    @Override
    public void activate(String username, Boolean activate) {
        Optional<User> user = userRepository.findByUsername(username);
        user.get().setActive(activate);
        userRepository.saveAndFlush(user.get());
    }

    @Override
    public void increaseBidderRating(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Integer updatedBidderRating = user.get().getBidderRating() + 1;
        user.get().setBidderRating(updatedBidderRating);
        userRepository.saveAndFlush(user.get());
    }

    @Override
    public void increaseSellerRating(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Integer updatedSellerRating = user.get().getSellerRating() + 1;
        user.get().setSellerRating(updatedSellerRating);
        userRepository.saveAndFlush(user.get());
    }
}
