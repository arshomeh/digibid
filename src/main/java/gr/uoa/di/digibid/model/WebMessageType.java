package gr.uoa.di.digibid.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by amehrabyan, gpozidis on 07/09/16.
 */
@Getter
@AllArgsConstructor
public enum WebMessageType {
    INCOMING("From"), OUTGOING("To");

    private final String name;
}
