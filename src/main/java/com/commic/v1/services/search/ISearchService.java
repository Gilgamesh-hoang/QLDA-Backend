/**
 * Class: ISearchServices
 *
 * @author ducvui2003
 * @created 16/10/24
 */
package com.commic.v1.services.search;

import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.responses.BookResponse;
import org.springframework.data.domain.Pageable;

public interface ISearchService {

    DataListResponse<BookResponse> getRankBy(String type, Pageable pageable);

    DataListResponse<BookResponse> getRankBy(String type, Integer categoryId, Pageable pageable);

    DataListResponse<BookResponse> getComicByPublishDate(Pageable pageable);

    DataListResponse<BookResponse> getComicByPublishDate(Pageable pageable, Integer categoryId);
}
