package com.example.rest;

import com.example.db.dao.ProductDao;
import com.example.db.entity.ProductEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductRestService {

    @Inject
    ProductDao productDao;

    @Path("/create")
    @POST
    public Response createProduct(ProductEntity productEntity) {
        productDao.createProduct(productEntity);
        return Response.ok(productEntity).build();
    }

    @Path("/update")
    @PUT
    public Response updateProduct(ProductEntity productEntity) {
        productDao.updateProduct(productEntity);
        return Response.ok(productEntity).build();
    }

    @Path("{id}")
    @GET
    public ProductEntity getProduct(@PathParam("id") Long id) {
        return productDao.getProductById(id);
    }

    @Path("/list")
    @GET
    public List<ProductEntity> getListOfProducts() {
        return productDao.getAllProducts();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productDao.deleteProductById(id);
        return Response.noContent().build();
    }
}