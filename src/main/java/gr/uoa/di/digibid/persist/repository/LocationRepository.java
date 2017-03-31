package gr.uoa.di.digibid.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import javax.transaction.Transactional;

import gr.uoa.di.digibid.persist.domain.Location;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select location from Location location where location.name = ?1 and location.latitude = ?2 and location.longitude = ?3")
    Optional<Location> findByNameAndByLatLon(String name, String latitude, String longitude);
}
