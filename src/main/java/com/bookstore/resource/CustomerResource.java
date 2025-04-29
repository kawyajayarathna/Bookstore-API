package com.bookstore.resource;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.Customer;
import com.bookstore.exception.CustomerNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    static final Map<Integer, Customer> customerStore = new HashMap<>();
    private static int customerIdCounter = 1;

    @POST
    public Response createCustomer(Customer customer) {
        // Check for duplicate email
        for (Customer existingCustomer : customerStore.values()) {
            if (existingCustomer.getEmail().equalsIgnoreCase(customer.getEmail())) {
                throw new InvalidInputException("Email already exists. Please use a different email.");
            }
        }

        // Input validations
        if (customer.getCustomerName() == null || customer.getCustomerName().isBlank()) {
            throw new InvalidInputException("Customer name cannot be null or empty.");
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new InvalidInputException("Email cannot be null or empty.");
        }
        if (customer.getPassword() == null || customer.getPassword().isBlank()) {
            throw new InvalidInputException("Password cannot be null or empty.");
        }

        int id = customerIdCounter++;
        customer.setCustomerId(id);
        customerStore.put(id, customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") int id) {
        Customer customer = customerStore.get(id);
        if (customer == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        return Response.ok(customer).build();
    }

    @GET
    public Response getAllCustomers() {
        return Response.ok(new ArrayList<>(customerStore.values())).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        if (!customerStore.containsKey(id)) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");

        // Input validation
        if(updatedCustomer.getCustomerName() == null || updatedCustomer.getCustomerName().isBlank()) {
            throw new InvalidInputException("Customer name cannot be null or empty.");
        }
        if(updatedCustomer.getEmail() == null || updatedCustomer.getEmail().isBlank()) {
            throw new InvalidInputException("Email cannot be null or empty.");
        }
        if (updatedCustomer.getPassword() == null || updatedCustomer.getPassword().isBlank()) {
            throw new InvalidInputException("Password cannot be null or empty.");
        }

        updatedCustomer.setCustomerId(id);
        customerStore.put(id, updatedCustomer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer removed = customerStore.remove(id);
        if (removed == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        return Response.ok(Map.of("message", "Customer with ID " + id + " was deleted")).build();
    }
}
