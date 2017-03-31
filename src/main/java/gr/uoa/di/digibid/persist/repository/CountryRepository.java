package gr.uoa.di.digibid.persist.repository;

import gr.uoa.di.digibid.persist.domain.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String name);

    List<Country> findByNameContaining(Sort sort, String name);
}