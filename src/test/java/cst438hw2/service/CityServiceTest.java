package cst438hw2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cst438hw2.domain.*;

@SpringBootTest
public class CityServiceTest {

  @MockBean
  private WeatherService weatherService;

  @Autowired
  private CityService cityService;

  @MockBean
  private CityRepository cityRepository;

  @MockBean
  private CountryRepository countryRepository;

  /*
   * Implement 3 separate tests to validate 1) city info based on input data and
   * match against expected data in mock 2) city not found in mock, and 3) check
   * if multiple entries of same city, and give entry with correct temp and time
   * info
   */
  @Test
  public void testCityFound() throws Exception {

    City test1 = new City(0, "AwesomeTown", "USA", "District 88", 88);

    // returns test1 mock city object from cityRepository
    given(cityRepository.findByName("AwesomeTown")).willReturn(List.of(test1));

    // returns country code and country from countryRepository
    given(countryRepository.findByCode("USA")).willReturn(new Country("USA", "United States"));

    // return temp and time of mock city from weatherservice
    given(weatherService.getTempAndTime("AwesomeTown"))
        .willReturn(new TempAndTime(304.27, 1594075040, 0));

    // call the cityService to retrieve converted data
    CityInfo inp_data = cityService.getCityInfo("AwesomeTown");

    CityInfo exp_data = new CityInfo(0, "AwesomeTown", "USA", "United States", "District 88", 88,
        88.0, "10:37:20 PM");

    // verify the actual data entered and retrieved is same as expected data
    // (exp_data)
    assertThat(inp_data).isEqualTo(exp_data);

  }

  @Test
  public void testCityNotFound() { // test will fail since actual input will not match null (empty)
                                   // expected data

    // returns no city as mock test object is null
    given(cityRepository.findByName("AwesomeTown")).willReturn(null);

    CityInfo inp_data = cityService.getCityInfo("AwesomeTown");
    CityInfo exp_data = null;

    assertThat(inp_data).isEqualTo(exp_data);
  }

  @Test
  public void testCityMultiple() {

    City test1 = new City(0, "AwesomeTown", "USA", "District 88", 88);
    City test2 = new City(1, "AwesomeTown", "USA", "District 13", 13);

    // return two entries with same city name
    given(cityRepository.findByName("AwesomeTown")).willReturn(List.of(test1, test2));

    given(countryRepository.findByCode("USA")).willReturn(new Country("USA", "United States"));
    given(weatherService.getTempAndTime("AwesomeTown"))
        .willReturn(new TempAndTime(304.27, 1594075040, 0));

    CityInfo inp_data = cityService.getCityInfo("AwesomeTown");
    CityInfo exp_data = new CityInfo(0, "AwesomeTown", "USA", "United States", "District 88", 88,
        88.0, "10:37:20 PM");

    assertThat(inp_data).isEqualTo(exp_data);
  }

}