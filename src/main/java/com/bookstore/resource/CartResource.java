package com.bookstore.resource;

import com.bookstore.model.*;
import com.bookstore.exception.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    static final Map<Integer, Cart> cartStore = new HashMap<>();

    @POST
    @Path("/items")
    public Response addCartItem(
            @PathParam("customerId") int customerId,
            CartItem item) {
        Cart cart = cartStore.computeIfAbsent(customerId, k -> new Cart(customerId));
        cart.addItem(item);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @DELETE
    @Path("/items/{isbn}")
    public Response removeCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("isbn") String isbn) {
        Cart cart = cartStore.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found for customer");
        cart.removeItem(isbn);
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{isbn}")
    public Response updateCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("isbn") String isbn,
            int quantity) {
        Cart cart = cartStore.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found for customer");
        cart.updateItem(isbn, quantity);
        return Response.ok(cart).build();
    }

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        Cart cart = cartStore.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found for customer");
        return Response.ok(cart).build();
    }

    public static Map<Integer, Cart> getCartStore() {
        return cartStore;
    }
}
