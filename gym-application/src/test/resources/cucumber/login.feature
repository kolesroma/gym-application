Feature: user logs in

  Scenario: client makes good POST request to /auth/login and get jwt
    When client makes good POST request to /auth/login
    Then the client receives status code of 200
    And the client receives jwt

  Scenario: client makes bad POST request to /auth/login and is getting suspicious
    When client makes bad POST request to /auth/login
    Then login failed so client is getting suspicious

  Scenario: client makes bad POST requests to /auth/login 5 times and is temporary banned to access server
    When client makes bad POST request to /auth/login 5 times
    Then client is temporary banned to access server