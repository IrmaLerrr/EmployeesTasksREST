package org.irmalerrr.employeeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    private EmployeeDto createEmployeeDto(Long id, String firstName) {
        LocalDateTime now = LocalDateTime.now();
        return new EmployeeDto(
                id, firstName, "lastName", "test@test.com",
                BigDecimal.valueOf(100_000.0), "1234567890", "position",
                now, now
        );
    }

    private CreateEmployeeDto createCreateEmployeeDto() {
        return new CreateEmployeeDto(
                "first", "lastName", "test@test.com",
                BigDecimal.valueOf(100_000.0), "81234567890", "position");
    }

    @Test
    @DisplayName("GET /api/employees/1 returns 200 OK and a valid JSON")
    void getEmployee_returnsValidResponseEntity() throws Exception {
        EmployeeDto mockEmp = createEmployeeDto(1L, "first");
        when(employeeService.getEmployee(1L)).thenReturn(mockEmp);

        mockMvc.perform(get("/api/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("first"))
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.salaryGross").exists())
                .andExpect(jsonPath("$.phoneNumber").exists())
                .andExpect(jsonPath("$.position").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    @DisplayName("GET /api/employees returns 200 OK and a valid JSON")
    void getEmployees_returnsValidResponseEntity() throws Exception {
        EmployeeDto mockEmp1 = createEmployeeDto(1L, "first");
        EmployeeDto mockEmp2 = createEmployeeDto(2L, "second");
        when(employeeService.getAllEmployees()).thenReturn(List.of(mockEmp1, mockEmp2));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("POST /api/employees returns 201 OK and a valid JSON")
    void createEmployee_returnsValidResponseEntity() throws Exception {
        CreateEmployeeDto mockEmp1 = createCreateEmployeeDto();
        EmployeeDto mockEmp2 = createEmployeeDto(1L, "first");
        when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(mockEmp2);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("first"));
    }

    @Test
    @DisplayName("PUT /api/employees/1 returns 200 OK and a valid JSON")
    void updateEmployee_returnsValidResponseEntity() throws Exception {
        CreateEmployeeDto mockEmp1 = createCreateEmployeeDto();
        EmployeeDto mockEmp2 = createEmployeeDto(1L, "first");
        when(employeeService.updateEmployee(eq(1L), any(CreateEmployeeDto.class))).thenReturn(mockEmp2);

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("first"));
    }

    @Test
    @DisplayName("Delete /api/employees returns 204 OK and a valid JSON")
    void deleteEmployee_returnsValidResponseEntity() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/employees with Blank firstName returns 400")
    void createEmployee_withBlankFirstName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setFirstName("");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with null firstName returns 400")
    void createEmployee_withNullFirstName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setFirstName(null);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with Blank lastName returns 400")
    void createEmployee_withBlankLastName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setLastName("");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with null lastName returns 400")
    void createEmployee_withNullLastName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setLastName(null);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with invalid email returns 400")
    void createEmployee_withInvalidEmail_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setEmail("test");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with invalid phoneNumber returns 400")
    void createEmployee_withInvalidPhoneNumber_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setPhoneNumber("0123456789");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with invalid salary returns 400")
    void createEmployee_withInvalidSalary_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setSalaryGross(BigDecimal.valueOf(-100000));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with Blank position returns 400")
    void createEmployee_withBlankPosition_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setPosition("");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees with null position returns 400")
    void createEmployee_withNullPosition_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setPosition(null);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with Blank firstName returns 400")
    void updateEmployee_withBlankFirstName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setFirstName("");

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with null firstName returns 400")
    void updateEmployee_withNullFirstName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setFirstName(null);

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with Blank lastName returns 400")
    void updateEmployee_withBlankLastName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setLastName("");

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with null lastName returns 400")
    void updateEmployee_withNullLastName_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setLastName(null);

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with invalid email returns 400")
    void updateEmployee_withInvalidEmail_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setEmail("test");

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with invalid phoneNumber returns 400")
    void updateEmployee_withInvalidPhoneNumber_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setPhoneNumber("0123456789");

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with invalid salary returns 400")
    void updateEmployee_withInvalidSalary_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setSalaryGross(BigDecimal.valueOf(-100000));

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with Blank position returns 400")
    void updateEmployee_withBlankPosition_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setPosition("");

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees with null position returns 400")
    void updateEmployee_withNullPosition_returnsBadRequest() throws Exception {
        CreateEmployeeDto mockEmp = createCreateEmployeeDto();
        mockEmp.setPosition(null);

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEmp)))
                .andExpect(status().isBadRequest());
    }
}
