package gr.uoa.di.digibid.service;

import java.util.List;
import java.util.Optional;

import gr.uoa.di.digibid.model.WebItemImage;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface ItemImageService {

    Optional<WebItemImage> getItemImageById(Long id);

    List<WebItemImage> getItemImageByItemId(Long itemId);

    WebItemImage createOrUpdate(WebItemImage webItemImage);
}
