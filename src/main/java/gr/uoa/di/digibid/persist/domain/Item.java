package gr.uoa.di.digibid.persist.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import static javax.persistence.CascadeType.ALL;

/**
 * Created by amehrabyan, gpozidis on 27/08/16.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
@AnalyzerDef(
        name = "itemAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class,
                        params = {
                                @Parameter(name = "language", value = "English")
                        }
                )
        })
@Entity
public class Item {

    protected static final String ID = "id";

    // Hibernate Search needs to store the entity identifier in the index for
    // each entity. By default, it will use for this purpose the field marked
    // with Id.
    @Id
    @Column(name = ID, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // You have to mark the fields you want to make searchable annotating them
    // with Field.
    // The parameter Store.NO ensures that the actual data will not be stored in
    // the index (mantaining the ability to search for it): Hibernate Search
    // will execute a Lucene query in order to find the database identifiers of
    // the entities matching the query and use these identifiers to retrieve
    // managed objects from the database.
    @Field
    @Analyzer(definition = "itemAnalyzer")
    @Column
    @NotNull
    private String name;

    @IndexedEmbedded
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_category_mapping",
            joinColumns = {
                    @JoinColumn(name = "item_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_category_id", referencedColumnName = ItemCategory.ID)
            }
    )
    private Set<ItemCategory> itemCategories = Sets.newHashSet();

    @Field
    @Column
    private Long price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = ALL)
    private List<Bid> bids = Lists.newArrayList();

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @NotNull
    private Location location;

    @Field
    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime started;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    @NonNull
    private LocalDateTime ends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_username")
    @NotNull
    private User seller;

    @Column
    private Long firstBid;

    @Field
    @Analyzer(definition = "itemAnalyzer")
    @Column
    @Lob
    @NotNull
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = ALL)
    private List<ItemImage> itemImages = Lists.newArrayList();

}
