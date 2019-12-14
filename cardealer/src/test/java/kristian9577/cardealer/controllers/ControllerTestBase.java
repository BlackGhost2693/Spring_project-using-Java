package kristian9577.cardealer.controllers;

import kristian9577.cardealer.base.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class ControllerTestBase extends BaseTest {
    @Autowired
    protected MockMvc mockMvc;
}