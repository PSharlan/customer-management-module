package com.itechart.devbooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@SqlConfig(dataSource = "pgTestDataSource")
@Sql(value = {"/create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returnAllCustomers() throws Exception {
        this.mockMvc.perform(get("/api/v1/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("john@firstblood.com")))
                .andExpect(content().string(containsString("pasha.sharlan@gmail.com")));

    }

    @Test
    public void returnCustomerById() throws Exception {
        this.mockMvc.perform(get("/api/v1/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("john@firstblood.com")))
                .andExpect(content().string(containsString("John")));
    }

    @Test
    public void notFoundCustomerById() throws Exception {
        this.mockMvc.perform(get("/api/v1/customers/00"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void returnCustomerByEmail() throws Exception {
        this.mockMvc.perform(get("/api/v1/customers/email/john@firstblood.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("john@firstblood.com")))
                .andExpect(content().string(containsString("John")));
    }

    @Test
    public void notFoundCustomerByEmail() throws Exception {
        this.mockMvc.perform(get("/api/v1/customers/email/00"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateNotExistingCustomer() throws Exception {
        this.mockMvc.perform(put("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"role\":\"USER\",\"name\":\"TestName\",\"lastName\":\"TestLastName\",\"email\":\"test@email\",\"password\":\"1234\",\"dateOfBirth\":\"2018-11-27\"}"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createNewCustomerAndDelete() throws Exception {
        this.mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"role\":\"USER\",\"name\":\"TestName\",\"lastName\":\"TestLastName\",\"email\":\"test@email.com\",\"password\":\"12345678\",\"dateOfBirth\":\"2018-11-27\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("TestName")))
                .andExpect(content().string(containsString("TestLastName")))
                .andExpect(content().string(containsString("USER")));

        MvcResult result = this.mockMvc.perform(get("/api/v1/customers/email/test@email.com"))
                .andReturn();
        String foundStr = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(foundStr, Map.class);
        int id = (Integer)map.get("id");

        this.mockMvc.perform(delete("/api/v1/customers/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/api/v1/customers/" + id))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


}
