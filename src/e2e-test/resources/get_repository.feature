Feature: Get repository data from REST endpoint

  Scenario: Get repository when owner not exists
    Given url 'http://localhost:9090/repositories/aaaaaaabbbbbbbbbbbbccccccccddddddddddddeeeeeeee/repository'
    When method GET
    Then status 404

  Scenario: Get repository when owner exists, but repository not
    Given url 'http://localhost:9090/repositories/testrepositories/repository123'
    When method GET
    Then status 404

  Scenario: Get existing repository
    Given url 'http://localhost:9090/repositories/testrepositories/repository1'
    When method GET
    Then status 200
    And match $ == {fullName: testrepositories/repository1, description: This is first repository, cloneUrl: https://github.com/testrepositories/repository1.git, stars:  '#? _ >= 1', createdAt: 2018-05-26T16:30:51}