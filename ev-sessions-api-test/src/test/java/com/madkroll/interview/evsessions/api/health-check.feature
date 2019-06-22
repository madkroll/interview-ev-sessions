Feature: Service is healthy

  Background:
    * url baseUrl

  Scenario: EV Sessions Service is healthy
    Given url baseUrl + '/health'
    When method get
    Then status 200
    And match response == {"status":"UP"}
