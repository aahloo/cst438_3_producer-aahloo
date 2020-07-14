package cst438hw2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import cst438hw2.domain.City;
import cst438hw2.domain.CityInfo;
import cst438hw2.domain.CityRepository;
import cst438hw2.domain.Country;
import cst438hw2.service.CityService;

@RunWith(SpringRunner.class)
@WebMvcTest(CityRestController.class) // Testing 'CityRestController class; class must be annotated
                                      // as WebMvcTest, not SpringBootTest
public class CityRestControllerTest {

  // declare as @MockBean those classes which will be stubbed in the test
  // These classes must be Spring components (such as Repositories)
  // or @Service classes.
  @MockBean
  private CityService cityService;

  @MockBean
  private CityRepository cityRepository;

  @Autowired
  private MockMvc mvc; // This class is used for make simulated HTTP requests to the class being
                       // tested.

  // This object will be magically initialized by the initFields method below.
  private JacksonTester<CityInfo> jsonCityAttempt;

  @BeforeEach
  public void setUpEach() {

    // MockitoAnnotations.initMocks(this);
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  public void testCityFound() throws Exception {
		
	CityInfo inp_data = new CityInfo( 1, "AwesomeTown", "USA", "United States", "District 88", 88, 304.27, "1594075040" );
		
	CityInfo exp_data = new CityInfo( 1, "AwesomeTown", "USA", "United States", "District 88", 88, 88.0, "3:37:20 PM");
		
	// cityService stub
	given(cityService.getCityInfo("AwesomeTown")).willReturn(exp_data);
		
	// execute query against mock inp_data
	MockHttpServletResponse response = mvc.perform( get("/api/cities/AwesomeTown").contentType(MediaType.APPLICATION_JSON)
                       					.content(jsonCityAttempt.write(inp_data).getJson()) ).andReturn().getResponse();
		
	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
	// verify response matches expected result
	assertThat(response.getContentAsString()).isEqualTo(jsonCityAttempt.write(exp_data).getJson());
  }
	
  @Test
  public void testCityNotFound() throws Exception {

    given(cityService.getCityInfo("AwesomeTown")).willReturn(null);

    MockHttpServletResponse response = mvc.perform(get("/api/cities/AwesomeTown")).andReturn()
        .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

}
