package gr.uoa.di.digibid.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import gr.uoa.di.digibid.model.WebItemImage;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.domain.ItemImage;
import gr.uoa.di.digibid.persist.repository.ItemImageRepository;
import gr.uoa.di.digibid.service.ItemImageService;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
@Transactional
public class ItemImageServiceImpl implements ItemImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemImageServiceImpl.class);

    @Autowired
    private ItemImageRepository itemImageRepository;

    @Override
    public Optional<WebItemImage> getItemImageById(Long id) {
        ItemImage itemImage = itemImageRepository.findOne(id);
        return itemImage != null ? Optional.ofNullable(DomainConverter.convert(itemImage)) : Optional.empty();
    }

    @Override
    public List<WebItemImage> getItemImageByItemId(Long itemId) {
        List<ItemImage> itemImages = itemImageRepository.findByItemId(itemId);
        return itemImages.stream().map(DomainConverter::convert).collect(Collectors.toList());
    }

    @Override
    public WebItemImage createOrUpdate(WebItemImage webItemImage) {
        return DomainConverter.convert(itemImageRepository.saveAndFlush(DomainConverter.convert(webItemImage)));
    }
}
