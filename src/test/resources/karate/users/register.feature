Feature: Register / Login

    Background:
        * url 'http://localhost:8080/api'


    Scenario: Successful register and login

        Given path '/users'
        And request { "user": { "email": "testing@training.com", "username": "TestingTraining", "password": "test" } }
        When method POST
        Then status 200

        Given path '/user'
        And header Authorization = 'Bearer ' + response.user.token
        When method GET
        Then status 200
        And match response.user.username == "TestingTraining"
