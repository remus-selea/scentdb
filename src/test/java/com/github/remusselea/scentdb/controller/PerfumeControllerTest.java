package com.github.remusselea.scentdb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2"})
@Disabled
class PerfumeControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void getAllPerfumesTest() throws Exception {
    String allPerfumesMock = FileUtils
        .readFileToString(new File("src/test/resources/mocks/perfumes/mockAllPerfumes.json"),
            StandardCharsets.UTF_8);

    this.mockMvc.perform(get("/scentdb/v1/perfumes"))
        .andExpect(status().isOk())
        .andExpect(content().string(allPerfumesMock))
        .andDo(print()).andReturn();
  }

  @Test
  void getPerfumeByIdTest() throws Exception {
    String perfumeMock = FileUtils
        .readFileToString(new File("src/test/resources/mocks/perfumes/mockPerfume.json"),
            StandardCharsets.UTF_8);

    this.mockMvc.perform(get("/scentdb/v1/perfumes/1"))
        .andExpect(status().isOk())
        .andExpect(content().string(perfumeMock))
        .andDo(print()).andReturn();
  }


}
