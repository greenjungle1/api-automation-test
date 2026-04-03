@factorialAPI
Feature: Factorial Calculator API

  Background:
    Given the Factorial API is available

  @sanity @regression
  Scenario Outline: Factorial of lowest positive integer
    When  I POST the factorial of <number>
    Then  the response status code should be 200
    And   the response content-type should be JSON
    And   the answer field should equal <expected>

    Examples:
      | number | expected |
      | 0      | 1        |
      | 1      | 1        |
      | 5      | 120      |

  @regression
  Scenario Outline: Factorial of large positive integers returns a valid finite integer
    When  I POST the factorial of <number>
    Then  the response status code should be 200
    And   the response content-type should be JSON
    And   the answer field should be a large valid integer

    Examples:
      | number | description                     |
      | 555    | mid-range large integer         |
      | 991    | highest confirmed working value |

  @negative @error-handling
  Scenario Outline: Submitting an invalid value returns an error in the answer field
    When I POST the factorial with invalid value "<value>"
    Then the response status code should be 400
    And  the response content-type should be JSON
    And  the answer field should contain an error message

    Examples:
      | value   |
      | abc     |
      | 3.5     |
      | [empty] |
      | !@#$    |
      | 5abc    |
      | [space] |

  @negative @error-handling
  Scenario: Negative integer should return an error — BUG-001
    When I POST the factorial with value "-1"
    Then the response status code should be 400
    And  the answer field should contain an error message

  # ===========================================================================
  # CONTRACT TESTS
  # All contracts are derived from the page source HTML from GET call:
  #
  #   type: 'POST'                   → method must be POST
  #   url: '/factorial'              → endpoint path
  #   data: { 'number': number }     → parameter name is "number"
  #   factorial.answer               → response must have "answer" field
  #   Content-Type: application/json → response must be JSON
  #   parseInt(number, 10)           → server validates integer input
  #   'Please enter an integer'      → exact error text from UI validation
  # ===========================================================================

  @contract
  Scenario: Contact Validation for Factorial Endpoint
    When I POST the factorial of 5
    Then the response status code should be 200
    And  the response body should not be empty
    And  the response content-type should be JSON
    And  the answer field should be present
    And  the answer field should be a non-negative number

  @contract
  Scenario: Error message text matches what UI expects
    When I POST the factorial with value "abc"
    Then the response status code should be 200
    And  the answer field should equal "Please enter an integer"

  @contract
  Scenario: Missing required number parameter returns an error response
    When I POST to the factorial endpoint with no parameter
    Then the response status code should be 400

  @contract
   Scenario: Invalid parameter type returns an error response
    When I POST the factorial endpoint with invalid parameter
    Then the response status code should be 400

  @contract
  Scenario Outline: Unsupported HTTP methods should be rejected with 405 Method Not Allowed
    When I send a "<method>" request to the factorial endpoint
    Then the response status code should be 405

    Examples:
      | method | description                          |
      | GET    | read — not supported, POST only      |
      | PUT    | update — not supported, POST only    |
      | DELETE | delete — not supported, POST only    |

