package org.fxclub.qa.influx;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.То;
import cucumber.api.java.ru.Тогда;
import gherkin.pickles.PickleRow;
import org.testng.Assert;

import java.util.Random;

public class Steps {

    private String URL_First;
    private String URL_Second;
    private String URL_concat;

    private int a;
    private int b;
    private int c;
    private int sum;

    @Дано("^первое число (\\d+)$")
    public void первое_число(int digit) throws Throwable {
        sleep();
        a = digit;
    }

    @Дано("^второе число (\\d+)$")
    public void второе_число(int digit) throws Throwable {
        sleep();
        b = digit;
    }

    @Дано("^третье число (\\d+)$")
    public void третье_число(int digit) throws Throwable {
        sleep();
        c = digit;
    }

    @Когда("^я их складываю$")
    public void я_их_складываю() throws Throwable {
        sleep();
        sum = a + b + c;
    }

    @Тогда("^сумма равна (\\d+)$")
    public void сумма_равна(int result) throws Throwable {
        sleep();
        Assert.assertEquals(result, sum);
    }

    @Дано("^сломанный сценарий$")
    public void сломанный_сценарий() throws Throwable {
        sleep();
        try {
            Object o = 1;
            String fail = (String) o;
        } catch (Exception e) {
            makeAttach(e.getMessage());
            throw e;
        }
    }

    @Когда("^отображается отчет$")
    public void отображается_отчет() throws Throwable {
        sleep();
    }

    @То("^видно исключение$")
    public void видно_исключение() throws Throwable {
        sleep();
    }

    @Given("^Anything in given with (.+)$")
    public void anything_in_given_with_dots_This_is_an_example(String text) throws Throwable {
        sleep();
    }

    @When("^whe run the scenario$")
    public void whe_run_the_scenario() throws Throwable {
        sleep();
    }

    @Then("^scenario name shuld be complete$")
    public void scenario_name_shuld_be_complete() throws Throwable {
        sleep();
    }

    @Given("^An URL (.+)$")
    public void an_URL(String URL) throws Throwable {
        sleep();
        this.URL_First = URL;
    }

    @Given("^another URL (.+)$")
    public void another_URL(String URL) throws Throwable {
        sleep();
        this.URL_Second = URL;
    }

    @When("^whe concatenate it$")
    public void whe_concatenate_it() throws Throwable {
        sleep();
        this.URL_concat = this.URL_First + this.URL_Second;
    }

    @Then("^Result should be (.+)$")
    public void result_should_be(String expected) throws Throwable {
        Assert.assertEquals(expected, URL_concat);
    }

    //@Attachment
    public String makeAttach(String text) {
        return text;
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(100 + new Random().nextInt(200));
    }

    @When("^test case has @TestCaseID$")
    public void testCaseHasTestCaseID() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // throw new PendingException();
    }

    @Then("^new test cycle will be created$")
    public void newTestCycleWillBeCreated() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // throw new PendingException();
    }

    @Дано("^(\\d+) числа:$")
    public void числа(int number, DataTable dataTable) throws Throwable {
        PickleRow row = dataTable.getPickleRows().get(0);
        первое_число(Integer.parseInt(row.getCells().get(0).getValue()));
        второе_число(Integer.parseInt(row.getCells().get(1).getValue()));
    }
}
