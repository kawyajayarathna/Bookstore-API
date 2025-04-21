package com.bookstore.resource;

import com.bookstore.model.*;
import com.bookstore.exception.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Map<Integer, List<Order>> orderStore = new HashMap<>();
    private static int orderIdCounter = 1;

    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        Cart cart = CartResource.getCartStore().get(customerId);
        if (cart == null || cart.getItems().isEmpty())
            throw new CartNotFoundException("Cannot place order with an empty cart.");

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            Book book = BookResource.bookStore.values().stream()
                    .filter(b -> b.getIsbn().equals(item.getIsbn()))
                    .findFirst()
                    .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + item.getIsbn() + " does not exist."));

            if (book.getStock() < item.getQuantity())
                throw new OutOfStockException("Not enough stock for book: " + book.getTitle());

            totalAmount += book.getPrice() * item.getQuantity();
            orderItems.add(new OrderItem(book.getIsbn(), item.getQuantity(), book.getPrice()));

            // Update stock
            book.setStock(book.getStock() - item.getQuantity());
        }

        Order order = new Order(orderIdCounter++, customerId, new Date(), totalAmount, orderItems);
        orderStore.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
        cart.getItems().clear();
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getOrders(@PathParam("customerId") int customerId) {
        List<Order> orders = orderStore.get(customerId);
        if (orders == null || orders.isEmpty())
            throw new CustomerNotFoundException("No orders found for customer.");
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(
            @PathParam("customerId") int customerId,
            @PathParam("orderId") int orderId) {
        List<Order> orders = orderStore.get(customerId);
        if (orders != null) {
            Optional<Order> order = orders.stream()
                    .filter(o -> o.getOrderId() == orderId)
                    .findFirst();
            if (order.isPresent()) return Response.ok(order.get()).build();
        }
        throw new CustomerNotFoundException("Order with ID " + orderId + " not found.");
    }

    public static Map<Integer, List<Order>> getOrderStore() {
        return orderStore;
    }
}

