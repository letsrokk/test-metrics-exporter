Feature: Тест allure

  @good
  Scenario: Сложение одинарное
    Given первое число 5
    And второе число 5
    When я их складываю
    Then сумма равна 10

  @good
  Scenario Outline: Сложение двух чисел
    Given первое число <first>
    And второе число <second>
    When я их складываю
    Then сумма равна <sum>

    Examples:
      | first | second | sum |
      | 6     | 6      | 12  |
      | 5     | 10     | 15  |

  @bad
  Scenario: Сценарий с неимплементированным шагом
    Given первое число 5
    When несуществующий шаг
    When я их складываю
    Then сумма равна 10