package gr.uoa.di.digibid.model;

import com.google.common.collect.Lists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.validation.constraints.NotNull;

import gr.uoa.di.digibid.persist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "email",
        "password",
        "passwordRepeated",
        "firstName",
        "lastName",
        "address",
        "phone",
        "ssn",
        "locationName",
        "countryName",
        "active",
        "creationDate",
        "roles"
}, ignoreUnknown = true)
public class WebUser {

    @NotEmpty
    @JacksonXmlProperty(isAttribute = true, localName = "UserID")
    private String username = "";

    @NotEmpty
    private String email = "";

    @NotEmpty
    private String password = "";

    @NotEmpty
    private String passwordRepeated = "";

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @NotEmpty
    private String address = "";

    @NotEmpty
    private String phone = "";

    @NotEmpty
    private String ssn = "";

    private String locationName = "";

    @JacksonXmlProperty(localName = "Location")
    private WebLocation location;

    @NotEmpty
    private String countryName = "";

    @JacksonXmlProperty(localName = "Country")
    private WebCountry country;

    @JacksonXmlProperty(isAttribute = true, localName = "Selling_Rating")
    private Integer sellerRating = 0;

    @JacksonXmlProperty(isAttribute = true, localName = "Bidding_Rating")
    private Integer bidderRating = 0;

    private boolean active = false;

    @NotEmpty
    private String creationDate;

    @NotNull
    private List<String> roles = Lists.newArrayList(User.Role.BIDDER.toString(), User.Role.SELLER.toString());
}
