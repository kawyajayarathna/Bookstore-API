package com.bookstore.resource;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.Author;
import com.bookstore.exception.AuthorNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    static final Map<Integer, Author> authorStore = new HashMap<>();
    private static int authorIdCounter = 1;

    @POST
    public Response createAuthor(Author author) {

        //Input Validation
        if(author.getAuthorName() == null || author.getAuthorName().isBlank()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
        if(author.getBiography() == null || author.getBiography().isBlank()) {
            throw new InvalidInputException("Biography cannot be empty");
        }
        int id = authorIdCounter++;
        author.setAuthorId(id);
        authorStore.put(id, author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Response getAllAuthors() {
        return Response.ok(new ArrayList<>(authorStore.values())).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthor(@PathParam("id") int id) {
        Author author = authorStore.get(id);
        if (author == null) throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        if (!authorStore.containsKey(id)) { throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        if (updatedAuthor.getAuthorName() == null || updatedAuthor.getAuthorName().isBlank()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
        if (updatedAuthor.getBiography() == null || updatedAuthor.getBiography().isBlank()) {
            throw new InvalidInputException("Biography cannot be empty");
        }
        updatedAuthor.setAuthorId(id);
        authorStore.put(id, updatedAuthor);
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author removed = authorStore.remove(id);
        if (removed == null) throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        return Response.ok(Map.of("message", "Author with ID " + id + " was deleted")).build();
    }
}
