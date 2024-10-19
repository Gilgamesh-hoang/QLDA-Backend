package com.commic.testCategory;

import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.CategoryResponse;
import com.commic.v1.repositories.ICategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;  // Giả lập HTTP request

    @Autowired
    private ICategoryRepository categoryRepository;
    @Test
    public void testGetAllCategories() throws Exception {

        mockMvc.perform(get("/api/v1/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].id").value(1)) // Kiểm tra ID của category đầu tiên
                .andExpect(jsonPath("$.result[0].name").value("Tưởng tượng")); // Kiểm tra tên của category đầu tiên
    }
    @Test
    void getAllCategories_ShouldReturnCategoriesFromDatabase() throws Exception {

        // Act: Gửi HTTP GET request đến API
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/categories", String.class);

        // Assert: Kiểm tra status code và dữ liệu trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        APIResponse<List<CategoryResponse>> apiResponse = objectMapper.readValue(
                response.getBody(), new TypeReference<>() {}
        );

        List<CategoryResponse> result = apiResponse.getResult();
        assertEquals(10, result.size());
        assertEquals("Tưởng tượng", result.get(0).getName());
        assertEquals("Tự lực", result.get(9).getName());
    }
}
