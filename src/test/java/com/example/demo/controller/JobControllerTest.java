package com.example.demo.controller;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;
import com.example.demo.service.JobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource(properties = "app.schedule-jobs-from-db.enabled=false")
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobServiceMock;

    @Test
    public void createJob() throws Exception {
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
        when(jobServiceMock.getJob(any())).thenReturn(new Job());

        mockMvc.perform(get("/v1/job/" + ThreadLocalRandom.current().nextInt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("get-job",
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).optional().description("Job id"),
                                        fieldWithPath("priority").type(JsonFieldType.NUMBER).optional().description("Job priority"),
                                        fieldWithPath("type").type(JsonFieldType.STRING).optional().description("Job type"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).optional().description("Job message (some detail information)"),
                                        fieldWithPath("createTime").type(JsonFieldType.STRING).optional().description("Job create time"),
                                        fieldWithPath("updateTime").type(JsonFieldType.STRING).optional().description("Job update time"),
                                        fieldWithPath("state").type(JsonFieldType.STRING).optional().description(Arrays.toString(JobState.values()))
                                )))
                .andReturn();
    }
}