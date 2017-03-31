package gr.uoa.di.digibid.persist.converter;

import gr.uoa.di.digibid.model.*;
import gr.uoa.di.digibid.persist.domain.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by amehrabyan, gpozidis on 03/09/16.
 */
public final class DomainConverter {

    public static WebUser convert(User user) {
        return WebUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .creationDate(LocalDateTimeConverter.toFormattedString(user.getCreationDate()))
                .active(user.isActive())
                .sellerRating(user.getSellerRating())
                .bidderRating(user.getBidderRating())
                .address(user.getAddress())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .locationName(user.getLocation().getName())
                .location(convert(user.getLocation()))
                .countryName(user.getLocation().getCountry().getName())
                .country(convert(user.getLocation().getCountry()))
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(Enum::toString).collect(toList()))
                .ssn(user.getSsn())
                .build();
    }

    public static User convert(WebUser webUser) {
        return User.builder()
                .username(webUser.getUsername())
                .password(webUser.getPassword())
                .creationDate(LocalDateTimeConverter.parse(webUser.getCreationDate()))
                .active(webUser.isActive())
                .sellerRating(webUser.getSellerRating())
                .bidderRating(webUser.getBidderRating())
                .address(webUser.getAddress())
                .email(webUser.getEmail())
                .firstName(webUser.getFirstName())
                .lastName(webUser.getLastName())
                .location(convert(webUser.getLocation()))
                .phone(webUser.getPhone())
                .roles(webUser.getRoles().stream().map(User.Role::valueOf).collect(toList()))
                .ssn(webUser.getSsn())
                .build();
    }

    public static WebBid convert(Bid bid) {
        return WebBid.builder()
                .id(bid.getId())
                .bidderUsername(bid.getBidder().getUsername())
                .time(LocalDateTimeConverter.toFormattedString(bid.getTime()))
                .amount(bid.getAmount())
                .webItemId(bid.getItem().getId())
                .build();
    }

    public static Bid convert(WebBid webBid) {
        return Bid.builder()
                .id(webBid.getId())
                .bidder(convert(webBid.getBidder()))
                .time(LocalDateTimeConverter.parse(webBid.getTime()))
                .amount(webBid.getAmount())
                .item(convert(webBid.getWebItem()))
                .build();
    }

    public static WebCountry convert(Country country) {
        return WebCountry.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    public static Country convert(WebCountry webCountry) {
        return Country.builder()
                .id(webCountry.getId())
                .name(webCountry.getName())
                .build();
    }

    public static Location convert(WebLocation webLocation) {
        return Location.builder()
                .id(webLocation.getId())
                .country(convert(webLocation.getCountry()))
                .latitude(webLocation.getLatitude())
                .longitude(webLocation.getLongitude())
                .name(webLocation.getName())
                .build();
    }

    public static WebLocation convert(Location location) {
        return WebLocation.builder()
                .id(location.getId())
                .country(convert(location.getCountry()))
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .name(location.getName())
                .build();
    }

    public static WebItem convert(Item item) {

        LocalDateTime ends = item.getEnds();
        LocalDateTime now = LocalDateTime.now();

        long duration = ends.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long diffInMinutes = duration < 0 ? 0 : TimeUnit.MILLISECONDS.toMinutes(duration);

        String timeLeft = diffInMinutes / 24 / 60 + "d " + diffInMinutes / 60 % 24 + "h " + diffInMinutes % 60 + "m";

        return WebItem.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice() != null ? String.valueOf(item.getPrice()) : "0")
                .currently(!CollectionUtils.isEmpty(item.getBids()) ? String.valueOf(item.getBids().stream().map(Bid::getAmount).max(Long::compareTo).get()) : "0")
                .currentBidUsername(!CollectionUtils.isEmpty(item.getBids()) ? max(item.getBids(), Comparator.comparing(Bid::getAmount)).getBidder().getUsername() : "")
                .numberOfBids(!CollectionUtils.isEmpty(item.getBids()) ? item.getBids().size() : 0)
                .description(item.getDescription())
                .itemCategories(!CollectionUtils.isEmpty(item.getItemCategories()) ? item.getItemCategories().stream().map(ItemCategory::getId).collect(toList()) : emptyList())
                .categories(!CollectionUtils.isEmpty(item.getItemCategories()) ? item.getItemCategories().stream().map(DomainConverter::convert).collect(Collectors.toSet()) : emptySet())
                .locationName(item.getLocation().getName())
                .location(convert(item.getLocation()))
                .longitude(item.getLocation().getLongitude())
                .latitude(item.getLocation().getLatitude())
                .countryName(item.getLocation().getCountry().getName())
                .country(convert(item.getLocation().getCountry()))
                .firstBid(item.getFirstBid() != null ? String.valueOf(item.getFirstBid()) : "")
                .webItemImages(!CollectionUtils.isEmpty(item.getItemImages()) ? item.getItemImages().stream().map(DomainConverter::convert).collect(toList()) : emptyList())
                .webBids(!CollectionUtils.isEmpty(item.getBids()) ? item.getBids().stream().map(Bid::getId).collect(toList()) : emptyList())
                .started(!StringUtils.isEmpty(item.getStarted()) ? LocalDateTimeConverter.toFormattedString(item.getStarted()) : "")
                .ends(LocalDateTimeConverter.toFormattedString(item.getEnds()))
                .endsFormatted(timeLeft)
                .sellerUsername(item.getSeller().getUsername())
                .seller(convert(item.getSeller()))
                .build();
    }

    public static WebItemCategory convert(ItemCategory itemCategory) {
        return WebItemCategory.builder()
                .id(itemCategory.getId())
                .name(itemCategory.getName())
                .build();
    }

    public static ItemCategory convert(WebItemCategory webItemCategory) {
        return ItemCategory.builder()
                .id(webItemCategory.getId())
                .name(webItemCategory.getName())
                .build();
    }

    public static WebItemImage convert(ItemImage itemImage) {
        return WebItemImage.builder()
                .id(itemImage.getId())
                .description(itemImage.getDescription())
                .data(convert(itemImage.getImage()))
                .webItemId(itemImage.getItem().getId())
                .build();
    }

    public static ItemImage convert(WebItemImage webItemImage) {
        return ItemImage.builder()
                .id(webItemImage.getId())
                .description(webItemImage.getDescription())
                .image(webItemImage.getDataInBytes())
                .item(convert(webItemImage.getWebItem()))
                .build();
    }

    public static Message convert(WebMessage webMessage) {
        return Message.builder()
                .id(webMessage.getId())
                .from(convert(webMessage.getFrom()))
                .to(convert(webMessage.getTo()))
                .subject(webMessage.getSubject())
                .content(webMessage.getContent())
                .viewed(webMessage.isRead())
                .deleted(webMessage.isDeleted())
                .time(LocalDateTimeConverter.parse(webMessage.getTime()))
                .build();
    }

    public static WebMessage convert(Message message) {
        return WebMessage.builder()
                .id(message.getId())
                .from(convert(message.getFrom()))
                .to(convert(message.getTo()))
                .subject(message.getSubject())
                .content(message.getContent())
                .read((message.isViewed()))
                .deleted((message.isDeleted()))
                .time(LocalDateTimeConverter.toFormattedString(message.getTime()))
                .build();
    }

    public static Item convert(WebItem webItem) {
        return Item.builder()
                .id((webItem.getId()))
                .name(webItem.getName())
                .bids(!CollectionUtils.isEmpty(webItem.getBids()) ? webItem.getBids().stream().map(DomainConverter::convert).collect(toList()) : emptyList())
                .firstBid(Long.valueOf(webItem.getFirstBid()))
                .price(StringUtils.isEmpty(webItem.getPrice()) ? 0L : Long.valueOf(webItem.getPrice()))
                .description(webItem.getDescription())
                .itemCategories(webItem.getCategories().stream().map(DomainConverter::convert).collect(Collectors.toSet()))
                .location(convert(webItem.getLocation()))
                .seller(convert(webItem.getSeller()))
                .started(StringUtils.isEmpty(webItem.getStarted()) ? null : LocalDateTimeConverter.parse(webItem.getStarted()))
                .ends(LocalDateTimeConverter.parse(webItem.getEnds()))
                .build();
    }

    private static String convert(byte[] image) {
        return "data:image/png;base64," + new String(Base64.encodeBase64(image));
    }
}