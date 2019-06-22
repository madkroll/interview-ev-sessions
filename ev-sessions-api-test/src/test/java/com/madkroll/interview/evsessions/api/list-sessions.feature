Feature: List Sessions Service endpoint

  Background:
    * url baseUrl

  Scenario: Listing existing sessions works and returns a non-empty response
    * def DataInjector = Java.type('com.madkroll.interview.evsessions.api.DataInjector')
    * json createdSessionFirst = DataInjector.newSession(baseUrl + '/chargingSessions/', 'station-id-test-listing-1')
    * json createdSessionSecond = DataInjector.newSession(baseUrl + '/chargingSessions/', 'station-id-test-listing-2')
    Given url baseUrl + '/chargingSessions'
    * header Accept = 'application/json'
    When method get
    Then status 200
    * match response.error == '#notpresent'
    * def SessionDataAssert = Java.type('com.madkroll.interview.evsessions.api.SessionDataAssert')
    * assert SessionDataAssert.isPositive(response.length);
