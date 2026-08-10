Feature: Membership signup

  Scenario: Sign up a new membership
    Given no membership exists for "jane@example.com"
    When Jane signs up for the STANDARD plan
    Then the membership is active
    And it can be viewed by its membership ID
