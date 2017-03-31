package gr.uoa.di.digibid.persist.repository;

import gr.uoa.di.digibid.persist.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    Optional<ItemImage> findByDescription(String description);

    List<ItemImage> findByItemId(Long itemId);
}
