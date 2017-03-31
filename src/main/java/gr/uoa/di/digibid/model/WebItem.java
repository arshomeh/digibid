package gr.uoa.di.digibid.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by amehrabyan, gpozidis on 30/08/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "itemCategories",
        "webBids",
        "countryName",
        "locationName",
        "longitude",
        "latitude",
        "endsFormatted",
        "sellerUsername",
        "webItemImages",
        "imageFile",
        "currentBidUsername"
}, ignoreUnknown = true)
public class WebItem {

    @JacksonXmlProperty(isAttribute = true, localName = "ItemID")
    private Long id;

    @NotEmpty
    @JacksonXmlProperty(localName = "Name")
    private String name = "";

    @NotEmpty
    private List<Long> itemCategories = Lists.newArrayList();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Category")
    private Set<WebItemCategory> categories = Sets.newHashSet();

    @JacksonXmlProperty(localName = "Currently")
    private String currently = "";
    private String currentBidUsername = "";

    @JacksonXmlProperty(localName = "Buy_Price")
    private String price = "";

    @NotEmpty
    @JacksonXmlProperty(localName = "First_Bid")
    private String firstBid = "";

    @JacksonXmlProperty(localName = "Number_of_Bids")
    private Integer numberOfBids = 0;

    private List<Long> webBids = Lists.newArrayList();

    @JacksonXmlElementWrapper(localName = "Bids")
    @JacksonXmlProperty(localName = "Bid")
    private List<WebBid> bids = Lists.newArrayList();

    @NotEmpty
    private String countryName = "";

    @JacksonXmlProperty(localName = "Country")
    private WebCountry country;

    @NotEmpty
    private String locationName = "";

    private String longitude = "";
    private String latitude = "";

    @JacksonXmlProperty(localName = "Location")
    private WebLocation location;

    @JacksonXmlProperty(localName = "Started")
    private String started;

    @NotEmpty
    @JacksonXmlProperty(localName = "Ends")
    private String ends = "";

    private String endsFormatted = "";

    private String sellerUsername;

    @JacksonXmlProperty(localName = "Seller")
    private WebUser seller;

    @NotEmpty
    @JacksonXmlProperty(localName = "Description")
    private String description = "";

    private List<WebItemImage> webItemImages = Lists.newArrayList();

    private MultipartFile imageFile;
}
