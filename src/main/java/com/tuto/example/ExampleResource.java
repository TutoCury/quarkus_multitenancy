package com.tuto.example;

import java.util.List;
import java.util.UUID;

import com.tuto.example.model.master.Property;
import com.tuto.example.model.tenant.Fruit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/example")
public class ExampleResource
{

    @GET
    @Path("/fruits")
    public List<Fruit> getFruits()
    {
        return Fruit.find("select f from Fruit f").list();
    }

    @POST
    @Path("/fruits")
    @Transactional
    public List<Fruit> createFruitExample()
    {
        var fruit = new Fruit();
        fruit.name = "Fruit " + UUID.randomUUID().toString();
        Fruit.persist(fruit);
        return Fruit.findAll().list();
    }

    @GET
    @Path("/properties")
    public List<Property> getProperties()
    {
        return Property.findAll().list();
    }

    @POST
    @Path("/properties")
    @Transactional
    public List<Property> createPropertyExample()
    {
        var prop = new Property();
        prop.id = UUID.randomUUID().toString();
        prop.value = "Value " + System.currentTimeMillis();
        Property.persist(prop);
        return Property.findAll().list();
    }

}
