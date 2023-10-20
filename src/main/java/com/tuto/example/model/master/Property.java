package com.tuto.example.model.master;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Property extends PanacheEntityBase
{

    @Id
    public String id;

    @Column(name = "val")
    public String value;

}
