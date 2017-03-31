package gr.uoa.di.digibid.persist.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    protected static final String ID = "id";

    @Id
    @Column(name = ID, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    private boolean viewed;

    @Column
    @NotNull
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    @NotNull
    private User to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_id")
    @NotNull
    private User from;

    @Column
    @NotNull
    private String subject;

    @Column
    @NotNull
    private String content;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    @NotNull
    private LocalDateTime time;
}