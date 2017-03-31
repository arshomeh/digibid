package gr.uoa.di.digibid.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by amehrabyan, gpozidis on 07/09/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebMessage {

    private Long id;
    private String toUsername;
    private String fromUsername;

    private WebUser to;
    private WebUser from;

    @NotEmpty
    private String subject = "";

    @NotEmpty
    private String content = "";

    private boolean read;
    private boolean deleted;
    private String time;
}
