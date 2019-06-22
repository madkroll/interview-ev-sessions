Feature: Finish Session Service endpoint

  Background:
    * url baseUrl

  Scenario: Finishing an existing session works and returns a valid response
    * def DataInjector = Java.type('com.madkroll.interview.evsessions.api.DataInjector')
    * json createdSession = DataInjector.newSession(baseUrl + '/chargingSessions/', 'station-id-test-finish')
    Given url baseUrl + '/chargingSessions/' + createdSession.id
    * header Accept = 'application/json'
    * request ''
    When method put
    Then status 200
    * match response.error == '#notpresent'
    * match response == { id: '#(createdSession.id)', stationId: '#(createdSession.stationId)', updatedAt: '#present', status: 'FINISHED' }
    * def SessionDataAssert = Java.type('com.madkroll.interview.evsessions.api.SessionDataAssert')
    * assert SessionDataAssert.isValidDateTime(response.updatedAt)