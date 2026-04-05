@factorialAPI
Feature: Factorial Calculator API

  Background:
    Given the Factorial API is available

  @sanity @regression
  Scenario Outline: Validate Factorial of small positive integers
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
  Scenario Outline: Validate Factorial of large positive integers
    When  I POST the factorial of <number>
    Then  the response status code should be 200
    And   the response content-type should be JSON
    And   the answer field should be a large valid integer

    Examples:
      | number |
      | 555    |
      | 991    |

  @regression
  Scenario Outline: Validate number with leading zero is accepted and returns correct factorial
    When I POST the factorial with value "<number>"
    Then the response status code should be 200
    And  the response content-type should be JSON
    And  the answer field should equal <expected>

    Examples:
      | number | expected |
      | 007    | 5040     |
      | 05     | 120      |

  @negative @error-handling @regression
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
      | 1 5     |

  # ===========================================================================
  # CONTRACT TESTS
  # All contracts derived from the page source HTML:
  #
  #   type: 'POST'                   → method must be POST
  #   url: '/factorial'              → endpoint path
  #   data: { 'number': number }     → parameter name is "number"
  #   factorial.answer               → response must have "answer" field
  #   Content-Type: application/json → response must be JSON
  #   parseInt(number, 10)           → server validates integer input
  #   'Please enter an integer'      → exact error text from UI validation
  # ===========================================================================

  @contract @sanity @regression
  Scenario: Validate Response contains the answer field and is not empty
    When I POST the factorial of 5
    Then the response status code should be 200
    And  the response body should not be empty
    And  the response content-type should be JSON
    And  the answer field should be present
    And  the answer field should be a non-negative number

  @contract @regression
  Scenario: Validate Error message matches what the UI expects
    When I POST the factorial with value "abc"
    Then the response status code should be 200
    And  the answer field should equal "Please enter an integer"

  @contract @regression
  Scenario: Validate Factorial with positive sign prefix
    When I POST the factorial with value "+5"
    Then the response status code should be 200
    And  the response content-type should be JSON
    And  the answer field should equal 120

  @contract @regression
  Scenario Outline: Validate Factorial with negative sign prefix
    When I POST the factorial with value "<value>"
    Then the response status code should be 200
    And  the answer field should equal <expected>

    Examples:
      | value | expected |
      | -1    | 1        |
      | -5    | 120      |

  @contract @regression
  Scenario: Validate POST request without the parameter
    When I POST to the factorial endpoint with no parameter
    Then the response status code should be 400

  @contract @regression
  Scenario Outline: Validate POST request with wrong parameter name is rejected
    When I POST the factorial with parameter name "<paramName>" and value "5"
    Then the response status code should be 400

    Examples:
      | paramName |
      | Number    |
      | NUMBER    |
      | num       |

  @contract @regression
  Scenario Outline: Validate unsupported HTTP methods
    When I send a "<method>" request to the factorial endpoint
    Then the response status code should be 405

    Examples:
      | method |
      | GET    |
      | PUT    |
      | DELETE |


