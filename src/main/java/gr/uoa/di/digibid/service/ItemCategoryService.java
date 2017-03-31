package gr.uoa.di.digibid.service;

import java.util.List;
import java.util.Optional;

import gr.uoa.di.digibid.model.WebItemCategory;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface ItemCategoryService {

    Optional<WebItemCategory> getItemCategoryByName(String itemCategoryName);

    List<WebItemCategory> getAllItemCategories();

    List<WebItemCategory> search(String categoryName);

    WebItemCategory createOrUpdate(WebItemCategory webItemCategory);
}
