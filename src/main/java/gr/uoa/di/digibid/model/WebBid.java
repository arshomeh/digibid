package gr.uoa.di.digibid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

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
        "webItemId",
        "webItem",
        "bidderUsername"
}, ignoreUnknown = true)
public class WebBid {

    private Long id;

    @JacksonXmlProperty(localName = "Bidder")
    private WebUser bidder;
    private String bidderUsername;

    private Long webItemId;
    private WebItem webItem;

    @JacksonXmlProperty(localName = "Time")
    private String time;

    @JacksonXmlProperty(localName = "Amount")
    private Long amount;
}
