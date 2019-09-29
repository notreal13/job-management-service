package com.example.demo.controller;

import com.example.demo.bean.JobSchedule;
import com.example.demo.service.JobScheduleService;
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

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestPropertySource(properties = "app.read-queued-jobs-from-db.enabled=false")
public class JobScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobScheduleService jobScheduleServiceMock;

    @Test
    public void createJobSchedule() throws Exception {
        when(jobScheduleServiceMock.createJobSchedule(any(), any(), any())).thenReturn(1L);

        mockMvc.perform(post("/v1/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .param("type", "INDEXING")
                .param("priority", "10")
                .param("cron", "0/55 * * * * ?"))
                .andDo(print())
                .andDo(
                        document("post-schedule")
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getJobSchedule() throws Exception {
        when(jobScheduleServiceMock.getJobSchedule(any())).thenReturn(new JobSchedule());

        mockMvc.perform(get("/v1/schedule/" + ThreadLocalRandom.current().nextInt(1_000))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("get-schedule",
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).optional().description("Job schedule id"),
                                        fieldWithPath("priority").type(JsonFieldType.NUMBER).optional().description("Job priority"),
                                        fieldWithPath("jobType").type(JsonFieldType.STRING).optional().description("Job type"),
                                        fieldWithPath("cronExpression").type(JsonFieldType.STRING).optional().description("Cron trigger expression")
                                )))
                .andReturn();
    }

    @Test
    public void deleteJobSchedule() throws Exception {
        mockMvc.perform(delete("/v1/schedule/" + ThreadLocalRandom.current().nextInt(1_000))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(
                        document("delete-schedule")
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}