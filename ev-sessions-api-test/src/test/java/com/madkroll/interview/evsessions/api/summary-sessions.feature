Feature: List Sessions Service endpoint

  Background:
    * url baseUrl

  Scenario: Fetching summary for sessions updated for last minute works
    * def DataInjector = Java.type('com.madkroll.interview.evsessions.api.DataInjector')
    * json createdSessionFirst = DataInjector.newSession(baseUrl + '/chargingSessions/', 'station-id-test-summary-1')
    * json createdSessionSecond = DataInjector.newSession(baseUrl + '/chargingSessions/', 'station-id-test-summary-2')
    * json finishedSessionFirst = DataInjector.finishSession(baseUrl + '/chargingSessions/' + createdSessionFirst.id)
    Given url baseUrl + '/chargingSessions/summary'
    * header Accept = 'application/json'
    When method get
    Then status 200
    * match response.error == '#notpresent'
    * def SessionDataAssert = Java.type('com.madkroll.interview.evsessions.api.SessionDataAssert')
    * assert SessionDataAssert.summaryIsCorrect(response.totalCount, response.startedCount, response.stoppedCount);
