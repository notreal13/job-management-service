package com.example.demo.controller;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;
import com.example.demo.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class JobControllerTest {

    @Autowired
    private
    MockMvc mockMvc;
    @Autowired
    private
    ObjectMapper mapper;

    @MockBean
    private JobService jobServiceMock;

    @Test
    public void saveJob() throws Exception {
        when(jobServiceMock.createJob(any(), any())).thenReturn(1L);
        mockMvc.perform(post("/v1/job")
                .contentType(MediaType.APPLICATION_JSON)
                .param("type", "DWH")
                .param("priority", "1"))
                .andDo(print())
                .andDo(
                        document("post-job")
                ).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getJobById() throws Exception {
        when(jobServiceMock.getJob(any())).thenReturn(createJob());

        mockMvc.perform(get("/v1/job/" + ThreadLocalRandom.current().nextInt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("get-job",
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).optional().description("Job id"),
                                        fieldWithPath("priority").type(JsonFieldType.NUMBER).optional().description("priority"),
                                        fieldWithPath("type").type(JsonFieldType.STRING).optional().description("type"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).optional().description("message"),
                                        fieldWithPath("createTime").type(JsonFieldType.STRING).optional().description("create time"),
                                        fieldWithPath("updateTime").type(JsonFieldType.STRING).optional().description("update time"),
                                        fieldWithPath("state").type(JsonFieldType.STRING).optional().description(Arrays.toString(JobState.values()))
                                )))
                .andReturn();
    }

    private Job createJob() {
        Job job = new Job();
        job.setPriority(0L);
        job.setType("Job type");
        return job;
    }
}