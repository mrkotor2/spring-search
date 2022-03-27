package com.project.siihomework.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Authorization")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter(AccessLevel.NONE)
    @Column(name = "create_date")
    @CreationTimestamp
    private Date createDate;

    @Setter(AccessLevel.NONE)
    @Column(name = "update_date")
    @UpdateTimestamp
    private Date updateDate;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Column(name = "actor")
    private String actor;

    @ElementCollection
    @CollectionTable(name = "transaction_item_mapping",
            joinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "data_name")
    @Column(name = "data")
    private Map<String, String> data;

}
