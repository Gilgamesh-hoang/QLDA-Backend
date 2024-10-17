/**
 * Class: DataListResponse
 *
 * @author ducvui2003
 * @created 16/10/24
 */
package com.commic.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DataListResponse<T> {
    List<T> data;
    Integer currentPage;
    Integer totalPages;
}
