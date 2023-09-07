Feature: API Testing for Weather Stations

  Scenario: Attempt to register a weather station without an API key
    Given I attempt to register a weather station without an API key
    Then verify that HTTP response code 401

  Scenario: Register two stations and verify their storage
    Given register two stations with the following details
      | external_id | name                     | latitude | longitude | altitude |
      | DEMO_TEST001 | Team Demo Test Station 1 | 33.33    | -122.43   | 222      |
      | DEMO_TEST002 | Team Demo Test Station 2 | 44.44    | -122.44   | 111      |
    Then verify that HTTP response code is 201
    And verify that the stations were successfully stored
      | external_id | name                     | latitude | longitude | altitude |
      | DEMO_TEST001 | Team Demo Test Station 1 | 33.33    | -122.43   | 222      |