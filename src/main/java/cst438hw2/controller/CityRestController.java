package cst438hw2.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cst438hw2.domain.*;
import cst438hw2.service.CityService;

@RestController
public class CityRestController {			
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	CityRepository cityRepository;
	
	@GetMapping("/api/cities/{city}")
	public ResponseEntity<CityInfo> getWeather (@PathVariable("city") String cityName) {
		
		CityInfo cityInfo = cityService.getCityInfo(cityName);
		
		if (cityInfo == null) {
			
			return new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND);
			
		}
		
		return new ResponseEntity<CityInfo>(cityInfo, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/api/cities/{city}")
	public ResponseEntity<CityInfo> deleteCity (@PathVariable("city") String name ) {
		
		List<City> cities = cityRepository.findByName(name);
		
		if (cities.size() == 0) {
			// city name not found.  Send 404 return code.
			return new ResponseEntity<CityInfo>( HttpStatus.NOT_FOUND);
		} 
		else {
			
			for (City c : cities) {
				cityRepository.delete(c);
			}
			// return 204,  request successful.  no content returned.
			return new ResponseEntity<CityInfo>( HttpStatus.NO_CONTENT);
			
		}
	}

}
