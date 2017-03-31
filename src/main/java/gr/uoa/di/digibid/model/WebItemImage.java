package gr.uoa.di.digibid.model;

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
public class WebItemImage {
    private Long id;
    private Long webItemId;
    private WebItem webItem;
    private String description;
    private String data;
    private byte[] dataInBytes;
}
