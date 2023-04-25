package tech.ada.adamon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestParam;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.util.TestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)  //comunicacao do teste com o banco de dados, sem mockar o comportamento
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdamonControllerTestIntegrado {

    @Autowired
    AdamonController adamonController;
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(2)
    void deveBuscarAdamons() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/adamon")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void deveCriarAdamonComSucesso() throws Exception {
        Adamon adamon = TestUtils.obterAdamon();
        String adamonAsString = TestUtils.jasonAsString(adamon);

        mockMvc.perform(MockMvcRequestBuilders.post("/adamon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adamonAsString).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertNotNull(adamon);
    }

    @Test
    @Order(3)
    void deveBuscarAdamonPorId() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/adamon"+ "/"+ id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1));
    }

}


