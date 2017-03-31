package gr.uoa.di.digibid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by amehrabyan, gpozidis on 02/09/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "id",
        "country"
}, ignoreUnknown = true)
public class WebLocation {

    private Long id;

    @JacksonXmlText
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "Latitude")
    private String latitude;

    @JacksonXmlProperty(isAttribute = true, localName = "Longitude")
    private String longitude;

    private WebCountry country;
}
