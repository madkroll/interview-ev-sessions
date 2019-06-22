Feature: Finish Session Service endpoint

  Background:
    * url baseUrl

  Scenario: Finishing an existing session works and returns a valid response
    * def DataInjector = Java.type('com.madkroll.interview.evsessions.api.DataInjector')
    * json createdSession = DataInjector.newSession(baseUrl + '/chargingSessions/', 'aaaaa')
    Given url baseUrl + '/chargingSessions/' + createdSession.id
    * header Accept = 'application/json'
    * request ''
    When method put
    Then status 200
    * match response.error == '#notpresent'
    * match response == { id: '#(createdSession.id)', stationId: '#present', updatedAt: '#present', status: 'FINISHED' }
    * def DateTimeAssert = Java.type('com.madkroll.interview.evsessions.api.DateTimeAssert')
    * assert DateTimeAssert.isValid(response.updatedAt)