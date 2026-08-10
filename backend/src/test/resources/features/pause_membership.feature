Feature: Pause membership

  Scenario: Pause an active membership
    Given Jane has an active membership
    When the membership is paused for 30 days
    Then it is paused today
    And it becomes active on the resume date
