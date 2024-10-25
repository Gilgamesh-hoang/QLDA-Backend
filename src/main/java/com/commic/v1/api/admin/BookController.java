package com.commic.v1.api.admin;

import com.commic.v1.dto.requests.BookRequest;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.services.book.IBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController(value = "BookControllerAdmin")
@RequestMapping("/api/v1/admin/books")
@PreAuthorize("hasAuthority('ADMIN')")
public class BookController {
    @Autowired
    private IBookService bookService;


    @PostMapping
    public ResponseEntity<APIResponse<Void>> addBook(@RequestBody @Valid BookRequest bookRequest) {
        APIResponse<Void> response = bookService.addBook(bookRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @PutMapping
    public ResponseEntity<APIResponse<Void>> updateBook(@RequestBody @Valid BookRequest bookRequest) {
        if (bookRequest.getId() == null) {
            return ResponseEntity.status(400).body(new APIResponse<>(400, "Id is required", null));
        }
        APIResponse<Void> response = bookService.updateBook(bookRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteBook(@PathVariable(name = "id") Integer id) {
        if (id == null) {
            return ResponseEntity.status(400).body(new APIResponse<>(400, "Id is required", null));
        }
        APIResponse<Void> response = bookService.deleteBook(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
