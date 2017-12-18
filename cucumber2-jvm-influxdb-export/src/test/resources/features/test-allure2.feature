Feature: Тест allure

  @good
  Scenario: Сложение одинарное
    Given первое число 5
    And второе число 5
    When я их складываю
    Then сумма равна 10

  @good
  Scenario Outline: New Scenario Outline
    Given первое число <first>
    And второе число <second>
    When я их складываю
    Then сумма равна <sum>

    Examples:
      | first | second | sum |
      | 6     | 6      | 12  |
      | 5     | 10     | 15  |

  @good
  Scenario: Added new test
    Given первое число 5
    And второе число 5
    When я их складываю
    Then сумма равна 10
