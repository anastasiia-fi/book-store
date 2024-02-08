package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dao.repository.CategoryRepository;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.mapper.impl.CategoryMapperImpl;
import com.example.bookstore.model.Category;
import com.example.bookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            Find category by valid id
            """)
    public void findById_WithValidId_ShouldReturnValidCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Name");
        category.setDescription("Description");
        CategoryDto categoryDto = new CategoryDto(categoryId, category.getName(),
                category.getDescription());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(categoryId);

        assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName("""
            Find category by invalid id
            """)
    public void findById_WithInvalidId_ShouldThrowException() {
        Long id = -1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.getById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find category with id " + id);
    }

    @Test
    @DisplayName("""
            Get all categories
            """)
    public void findAll_WithValidData_ShouldReturnAllCategories() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Name");
        category.setDescription("Description");

        Pageable pageable = Pageable.ofSize(10);
        List<Category> categories = List.of(category);

        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(categories));

        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertThat(actual.get(0))
                .hasFieldOrPropertyWithValue("name", "Name")
                .hasFieldOrPropertyWithValue("description", "Description");
    }

    @Test
    @DisplayName("""
            Create new category
            """)
    public void createCategory_WithValidData_ShouldReturnCategory() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Name",
                "Description"
        );
        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        when(categoryMapper.toCategory(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto actual = categoryService.save(requestDto);

        Assertions.assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "Name")
                .hasFieldOrPropertyWithValue("description", "Description");
    }

    @Test
    @DisplayName("""
            Delete category by valid id
            """)
    public void deleteById_WithValidId_ShouldDeleteCategory() {
        Long id = 1L;
        doNothing().when(categoryRepository).deleteById(id);
        categoryService.deleteById(id);
        verify(categoryRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(categoryRepository);
    }
}
