package com.bookstore.resource;

import com.bookstore.exception.OutOfStockException;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/customers/{customerId}/cart/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private static final Map<Integer, Cart> cartStore = new ConcurrentHashMap<>();

    public static Map<Integer, Cart> getCartStore() {
        return cartStore;
    }

    @POST
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        Book book = BookResource.getBookByIsbn(item.getIsbn());

        if (book == null) {
            throw new NotFoundException("ISBN not found: " + item.getIsbn());
        }

        if (item.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        if (item.getQuantity() > book.getStock()) {
            throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
        }

        Cart cart = cartStore.computeIfAbsent(customerId, id -> new Cart(customerId));
        cart.addItem(item);
        return Response.ok(cart).build();
    }

    @GET
    public Response getCartItems(@PathParam("customerId") int customerId) {
        Cart cart = cartStore.get(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cart is empty or not found for customer ID: " + customerId)
                    .build();
        }
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/{isbn}")  // Use relative path for 'isbn' at the method level
    public Response updateItemQuantity(
            @PathParam("customerId") int customerId,
            @PathParam("isbn") String isbn,
            CartItem item) {  // Accept CartItem which contains isbn and quantity

        // Get the cart for the customer
        Cart cart = cartStore.get(customerId);

        // If cart does not exist, create a new one
        if (cart == null) {
            cart = new Cart(customerId);
            cartStore.put(customerId, cart);  // Store the new cart for the customer
        }

        // Get the book by ISBN to validate if it's in stock
        Book book = BookResource.getBookByIsbn(isbn);
        if (book == null) {
            throw new NotFoundException("Book not found with ISBN: " + isbn);
        }

        // Validate the quantity from the CartItem
        if (item.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // Check if the requested quantity is available in stock
        if (item.getQuantity() > book.getStock()) {
            throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
        }

        // Update the quantity in the cart
        cart.updateItem(isbn, item.getQuantity());

        // Return the updated cart
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteItem(@PathParam("customerId") int customerId, @PathParam("isbn") String isbn) {
        Cart cart = cartStore.get(customerId);
        if (cart == null) {
            throw new NotFoundException("Cart not found for customer: " + customerId);
        }

        boolean removed = cart.removeItem(isbn);
        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Item not found in cart with ISBN: " + isbn)
                    .build();
        }

        String message = isbn + " was removed from the cart";
        return Response.ok(message).build();  // 200 OK with message
    }

}
