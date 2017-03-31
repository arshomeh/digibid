package gr.uoa.di.digibid.persist.domain;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by amehrabyan, gpozidis on 27/08/16.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Location {

    protected static final String ID = "id";

    @Id
    @Column(name = ID, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field
    @Column
    private String name;

    @Field
    @Longitude
    @Column
    private String longitude;

    @Field
    @Latitude
    @Column
    private String latitude;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    @NotNull
    private Country country;
}
