Feature: Submit Session Service endpoint

  Background:
    * url baseUrl

  Scenario: Submitting a new session works and returns a valid response
    * def stationId = 'submit-session-test-id'
    Given url baseUrl + '/chargingSessions'
    * header Accept = 'application/json'
    * request { stationId: '#(stationId)' }
    When method post
    Then status 200
    * match response.error == '#notpresent'
    * match response == { id: '#uuid', stationId: '#(stationId)', updatedAt: '#present' }
    * def SessionDataAssert = Java.type('com.madkroll.interview.evsessions.api.SessionDataAssert')
    * assert SessionDataAssert.isValidDateTime(response.updatedAt)
