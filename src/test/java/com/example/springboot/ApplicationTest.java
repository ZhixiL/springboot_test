//Name: Zhixi Lin
//FSUID: zl19
//CEN4020-0004
//Assignment: Unit Tests
//Due Date: 3/24/2021

package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test //Delete Functionality Unit Test
    void testDelete() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=toBeDeleted")).andReturn(); //user post the string
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("toBeDeleted"))); //Check if the string is posted correctly

        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=toBeDeleted")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("toBeDeleted"))));
    }

    @Test//Delete Case Sensitivity Functionality Unit Test
    void testDeleteCaseSensitivity() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=TEST")).andReturn(); //upper case input
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=test")).andReturn(); //lower case input

        //Check if inputs is correctly stored
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("TEST")));
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("test")));

        //run the delete twice, so if delete is not case sensitive, then both will be deleted
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=test")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=test"))
                .andExpect(content().string(containsString("does not exist")));
        //Additional Test, if delete is case sensitive, the second time this delete the post shouldn't
        //be successful

        //Now check if the upper case still exist and lower case has been deleted. If so, delete() is case sensitive.
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("TEST")));
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("test"))));

        //clear the post_log
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=TEST")).andReturn();
    }
}