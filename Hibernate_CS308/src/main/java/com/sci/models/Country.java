package com.sci.models;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "Countries")
@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor

public class Country implements Serializable {

    private static final long serialVersionUID = -4275743947797911430L;

    @Id
    @Column(name = "COUNTRY_ID", columnDefinition = "CHAR(2)")
    private String countryId;
    @Column(name = "COUNTRY_NAME")
    private String countryName;
    @Column(name = "REGION_ID")
    private Integer regionId;

    //Many Countries belong to one region:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID", insertable = false, updatable = false)
    private Region region;

    //One country has many Locations:
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID", insertable = false, updatable = false)
    private List<Location> locations;
}
