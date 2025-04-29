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
        Cart cart = (Cart) CartResource.getCartStore().get(customerId);

        if (cart == null)
            throw new CartNotFoundException("Cart not found for customer with ID " + customerId);

        if (cart.getItems().isEmpty())
            throw new InvalidInputException("Cannot place order with an empty cart.");

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        // Group cart items by ISBN and sum their quantities
        Map<String, Integer> totalQuantities = new HashMap<>();
        for (CartItem item : cart.getItems()) {
            if (item.getIsbn() == null || item.getIsbn().isBlank())
                throw new InvalidInputException("Invalid ISBN in cart item.");

            if (item.getQuantity() <= 0)
                throw new InvalidInputException("Quantity must be greater than 0 for ISBN: " + item.getIsbn());

            totalQuantities.merge(item.getIsbn(), item.getQuantity(), Integer::sum);
        }

        // Validate total stock availability
        for (Map.Entry<String, Integer> entry : totalQuantities.entrySet()) {
            String isbn = entry.getKey();
            int requestedQuantity = entry.getValue();

            Book book = BookResource.bookStore.values().stream()
                    .filter(b -> b.getIsbn().equals(isbn))
                    .findFirst()
                    .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + isbn + " does not exist."));

            if (book.getStock() < requestedQuantity)
                throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
        }

        // If all stock is valid, proceed with order creation
        for (CartItem item : cart.getItems()) {
            Book book = BookResource.bookStore.values().stream()
                    .filter(b -> b.getIsbn().equals(item.getIsbn()))
                    .findFirst()
                    .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + item.getIsbn() + " does not exist."));

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
            throw new OrderNotFoundException("No orders found for customer ID " + customerId);
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(
            @PathParam("customerId") int customerId,
            @PathParam("orderId") int orderId) {
        List<Order> orders = orderStore.get(customerId);
        if (orders != null) {
            return orders.stream()
                    .filter(o -> o.getOrderId() == orderId)
                    .findFirst()
                    .map(Response::ok)
                    .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found for customer " + customerId))
                    .build();
        }
        throw new CustomerNotFoundException("Customer with ID " + customerId + " has no orders.");
    }

    public static Map<Integer, List<Order>> getOrderStore() {
        return orderStore;
    }
}



