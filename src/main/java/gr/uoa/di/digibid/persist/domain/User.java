package gr.uoa.di.digibid.persist.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
 * Created by amehrabyan, gpozidis on 27/08/16.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @Column(updatable = false)
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private String phone;

    @Column
    @NotNull
    private String address;

    @Column
    @NotNull
    private String ssn; //aka AFM

    @Column
    @NotNull
    private boolean active;

    @Column(name = "seller_rating")
    @NotNull
    private Integer sellerRating;

    @Column(name = "bidder_rating")
    @NotNull
    private Integer bidderRating;

    @Column(name = "creation_date")
    @Convert(converter = LocalDateTimeConverter.class)
    @NotNull
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    public enum Role {
        GUEST, SELLER, BIDDER, ADMIN
    }
}
