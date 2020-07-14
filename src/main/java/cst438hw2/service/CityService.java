package cst438hw2.service;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cst438hw2.domain.*;

// class that uses CountryRepository and CityRepository to obtain information on the city and country
@Service
public class CityService {

  @Autowired
  private CityRepository cityRepository;
  
  @Autowired
  private CountryRepository countryRepository;
  
  @Autowired
  private WeatherService weatherService;
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private FanoutExchange fanout;  

  public CityInfo getCityInfo(String cityName) { // method to return relative information of
                                                 // requested city from CityInfo class

    List<City> cities = cityRepository.findByName(cityName);

    if (cities == null || cities.isEmpty()) { // returns null for CityServiceTest Test #2
                                              // (CityNotFound)
      return null;
    }

    TempAndTime t_detail = weatherService.getTempAndTime(cityName);

    City location = cities.get(0);
    Country country = countryRepository.findByCode(location.getCountryCode());

    DateFormat t_format = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
    t_format.setTimeZone(TimeZone.getTimeZone("GMT"));
    long raw_time = (t_detail.time + t_detail.timezone) * 1000;
    String time = t_format.format(raw_time);

    Double temp = t_detail.temp;
    temp = (double) Math.round((temp - 273.15) * 9.0 / 5.0 + 32.0); // convert kelvin to farenheit;
                                                                    // long to double value
    CityInfo cityInfo = new CityInfo(location, country.getName(), temp, time);

    return cityInfo;

  }
  
  public void requestReservation (String cityName, String level, String email) {
	  
	  String msg = "{\"cityName\": \"" + cityName + "\" \"level\": \"" + level + "\" \"email\": \"" + email + "\"}";
	  
	  System.out.println("Sending message: " + msg);
	  
	  rabbitTemplate.convertSendAndReceive(fanout.getName(), "", msg);
	  
  }

}
