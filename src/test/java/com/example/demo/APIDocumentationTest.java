package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 0, stubs = "file:stubs/mappings", files = "file:stubs/")
@AutoConfigureMockMvc
@AutoConfigureRestDocs(
        outputDir = "build/generated-snippets",
        uriHost = "api.mydomain.com",
        uriScheme = "https", uriPort = 443)
@TestPropertySource(properties = {"stub.server.url=http://localhost:${wiremock.server.port}"})
public class APIDocumentationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void formPostParamsAsBodySnippetsInsertQueryStringParams() throws Exception {

        mockMvc.perform(post("/api/auth/brand/{brand}/protocol/openid-connect/token", "demobrand").contextPath("/api")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("grant_type=client_credentials&client_id=pub-ivr-democlient&client_secret=470d588e-29b9-4f10-a04b-28f52addfb25"))
                .andExpect(status().isOk())
                .andDo(document("formPostParamsAsBodySnippetsInsertQueryStringParams",
                        pathParameters(
                                parameterWithName("brand").description("assigned brand")
                        )
                        ,requestParameters(
                                parameterWithName("grant_type").description("`client_credentials` grant type"),
                                parameterWithName("client_secret").description("provide the `client_secret` that was issued"),
                                parameterWithName("client_id").description("provide the `client_id` that was issued")
                        )
                ));
    }
}
