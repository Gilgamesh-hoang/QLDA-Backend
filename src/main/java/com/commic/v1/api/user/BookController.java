/**
 * Class: BookController
 *
 * @author ducvui2003
 * @created 16/10/24
 */
package com.commic.v1.api.user;

import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.BookResponse;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.services.search.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    @Autowired
    ISearchService searchServices;

    @GetMapping("/rank")
    public APIResponse<DataListResponse<BookResponse>> rank(@RequestParam(name = "page", defaultValue = "1") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                                            @RequestParam(name = "type", defaultValue = "") String type,
                                                            @RequestParam(name = "categoryId", defaultValue = "0") Integer categoryId) {

        Pageable pageable;
        pageable = PageRequest.of(page - 1, size);
        DataListResponse<BookResponse> items;
        if (categoryId == 0)
            items = searchServices.getRankBy(type, pageable);
        else items = searchServices.getRankBy(type, categoryId, pageable);
        APIResponse<DataListResponse<BookResponse>> apiResponse = new APIResponse<>();
        apiResponse.setCode(ErrorCode.FOUND.getCode());
        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
        apiResponse.setResult(items);
        return apiResponse;
    }
    @GetMapping("/newComic")
    public APIResponse<DataListResponse<BookResponse>> getNewComicOrderByPublishDate(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                        @RequestParam(name = "categoryId", defaultValue = "0") Integer categoryId) {
        Pageable pageable;
        pageable = PageRequest.of(page - 1, size);
        DataListResponse<BookResponse> items;
        if (categoryId == 0)
            items = searchServices.getComicByPublishDate(pageable);
        else
            items = searchServices.getComicByPublishDate(pageable, categoryId);
        APIResponse<DataListResponse<BookResponse>> apiResponse = new APIResponse<>();
        apiResponse.setCode(ErrorCode.FOUND.getCode());
        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
        apiResponse.setResult(items);
        return apiResponse;
    }
}
