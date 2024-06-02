package com.ai.xstack.kolesnyk.cucumber;

import com.ai.xstack.kolesnyk.AiDrivenXStackApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/cucumber")
@CucumberContextConfiguration
@SpringBootTest(classes = AiDrivenXStackApplication.class)
public class CucumberIntegrationTest {
}
