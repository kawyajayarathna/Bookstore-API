package com.bookstore.resource;

import com.bookstore.model.Book;
import com.bookstore.exception.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    static final Map<Integer, Book> bookStore = new HashMap<>();
    private static int bookIdCounter = 1;

    @POST
    public Response createBook(Book book) {
        //Input Validation
        if(book.getTitle() == null || book.getTitle().isBlank()) {
            throw new InvalidInputException("Title cannot be empty");
        }
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new InvalidInputException("Isbn cannot be empty");
        }
        if (book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        if (book.getAuthorId() <= 0){
            throw new InvalidInputException("Author ID must be a positive integer.");
        }
        int id = bookIdCounter++;
        book.setBookId(id);
        bookStore.put(id, book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(new ArrayList<>(bookStore.values())).build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") int id) {
        Book book = bookStore.get(id);
        if (book == null) throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        if (!bookStore.containsKey(id)) throw new BookNotFoundException("Book with ID " + id + " does not exist.");

        if(updatedBook.getTitle() == null || updatedBook.getTitle().isBlank()) {
            throw new InvalidInputException("Title cannot be empty");
        }
        if(updatedBook.getIsbn() == null || updatedBook.getIsbn().isBlank()) {
            throw new InvalidInputException("Isbn cannot be empty");
        }
        if(updatedBook.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        if(updatedBook.getAuthorId() <= 0){
            throw new InvalidInputException("Author ID must be a positive integer.");
        }
        updatedBook.setBookId(id);
        bookStore.put(id, updatedBook);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book removed = bookStore.remove(id);
        if (removed == null) {throw new BookNotFoundException("Book with ID " + id + " does not exist."); }
        return Response.ok(Map.of("message", "Book with ID " + id + " was deleted")).build();
    }

    public static List<Book> getBooksByAuthor(int authorId) {
        List<Book> list = new ArrayList<>();
        for (Book b : bookStore.values()) {
            if (b.getAuthorId() == authorId) list.add(b);
        }
        return list;
    }
}
