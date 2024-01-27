package com.example.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dao.repository.CategoryRepository;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @SneakyThrows
    @AfterEach
    void afterAll(
            @Autowired DataSource dataSource
    ) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-all-categories.sql"));
        }
    }

    @Test
    @DisplayName("Create a new category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createCategory_WithValidData_ShouldCreateValidCategory() throws Exception {
        long id = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Test category",
                "Test Description"
        );

        String jsonRequest;
        try {
            jsonRequest = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't read request", e);
        }

        MvcResult result = mockMvc.perform(
                        post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto expected = new CategoryDto(id, requestDto.name(), requestDto.description());

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        assertEquals(actual.name(), expected.name());
        assertEquals(actual.description(), expected.description());
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void findAllCategories_WithValidData_ShouldReturnTwoCategories() throws Exception {

        CategoryDto first = new CategoryDto(1L, "Category1", "Description1");
        CategoryDto second = new CategoryDto(2L, "Category2", "Description2");

        List<CategoryDto> expected = List.of(first, second);

        MvcResult result = mockMvc.perform(
                        get("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<CategoryDto>>(){});
        assertThat(actual.contains(expected.get(0)));
        assertThat(actual.contains(expected.get(1)));
    }

    @Test
    @DisplayName("Get category by id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void getCategoryById_WithValidData_ShouldReturnValidCategory() throws Exception {
        long id = 1L;

        MvcResult result = mockMvc.perform(
                        get("/api/categories/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "first")
                .hasFieldOrPropertyWithValue("description", "description1");
    }

    @Test
    @DisplayName("Get category by invalid id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void getCategoryById_WithInvalidData_ShouldThrowException() throws Exception {
        long id = 100L;

        MvcResult result = mockMvc.perform(
                        get("/api/categories/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(result.getResolvedException().getMessage(),
                ("Can't find category with id " + id));
    }

    @Test
    @DisplayName("Delete category by valid id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteCategoryById_WithValidData_ShouldDeleteCategory() throws Exception {
        long id = 1L;
        MvcResult result = mockMvc.perform(
                        delete("/api/categories/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        assertThat(categoryRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("Delete category without admin role")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteCategoryById_WithoutAdminRole_ShouldThrowException() throws Exception {
        long id = 1L;
        MvcResult result = mockMvc.perform(
                        delete("/api/categories/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
