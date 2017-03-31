package gr.uoa.di.digibid.persist.domain;

import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Country {

    protected static final String ID = "id";

    @Id
    @Column(name = ID, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field
    @Column
    @NotNull
    private String name;
}
