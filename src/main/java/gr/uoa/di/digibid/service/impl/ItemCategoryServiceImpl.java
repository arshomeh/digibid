package gr.uoa.di.digibid.service.impl;

import gr.uoa.di.digibid.model.WebItemCategory;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.domain.ItemCategory;
import gr.uoa.di.digibid.persist.repository.ItemCategoryRepository;
import gr.uoa.di.digibid.service.ItemCategoryService;
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
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Override
    public Optional<WebItemCategory> getItemCategoryByName(String itemCategoryName) {
        Optional<ItemCategory> country = itemCategoryRepository.findByName(itemCategoryName);
        return country.isPresent() ? Optional.ofNullable(DomainConverter.convert(country.get())) : Optional.empty();
    }

    @Override
    public List<WebItemCategory> getAllItemCategories() {
        return itemCategoryRepository.findAll(new Sort("name")).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public List<WebItemCategory> search(String categoryName) {
        return itemCategoryRepository.findByNameContaining(new Sort("name"), categoryName).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public WebItemCategory createOrUpdate(WebItemCategory webItemCategory) {
        return DomainConverter.convert(itemCategoryRepository.saveAndFlush(DomainConverter.convert(webItemCategory)));
    }
}
