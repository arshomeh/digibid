package gr.uoa.di.digibid.persist.repository;

import gr.uoa.di.digibid.persist.domain.Country;
import gr.uoa.di.digibid.persist.domain.ItemCategory;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    List<ItemCategory> findByNameContaining(Sort sort, String name);

    Optional<ItemCategory> findByName(String name);
}