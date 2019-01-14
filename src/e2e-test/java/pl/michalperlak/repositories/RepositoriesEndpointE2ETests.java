package pl.michalperlak.repositories;

import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import pl.michalperlak.repositories.infrastructure.E2ETestBase;

@RunWith(Karate.class)
@CucumberOptions(features = "classpath:get_repository.feature")
public class RepositoriesEndpointE2ETests extends E2ETestBase {

}
