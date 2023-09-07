package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.Assert.assertEquals;

public class StepDefinition {
    private Response response;
    private JSONArray storedStations;

    @Given("I attempt to register a weather station without an API key")
    public void registerStationWithoutApiKey() {
        response = RestAssured.given()
                .contentType("application/json")
                .post("https://api.openweathermap.org/data/3.0/stations");
    }

    @Then("verify that HTTP response code {int}")
    public void verify_that_http_response_code(int expectedResponseCode) {
        assertEquals(expectedResponseCode, response.getStatusCode());
    }

    @Given("register two stations with the following details")
    public void registerStations(DataTable stationData) {

        JSONArray stations = new JSONArray(stationData.asLists());
        for (int i = 0; i < stations.length(); i++) {
            JSONObject station = stations.getJSONObject(i);
            RestAssured.given()
                    .contentType("application/json")
                    .body(station.toString())
                    .post("https://api.openweathermap.org/data/3.0/stations");
        }
    }

    @Then("verify that HTTP response code is {int}")
    public void verifyResponseCode(int expectedResponseCode) {
        assertEquals(expectedResponseCode, response.getStatusCode());
    }

    @Then("verify that the stations were successfully stored")
    public void verifyStationsStored(DataTable expectedStations) {
        storedStations = new JSONArray(response.getBody().asString());
        JSONArray expectedStationsArray = new JSONArray(expectedStations.asLists());

        assertEquals(expectedStationsArray.length(), storedStations.length());

        for (int i = 0; i < expectedStationsArray.length(); i++) {
            JSONObject expectedStation = expectedStationsArray.getJSONObject(i);
            JSONObject storedStation = findStationByExternalId(expectedStation.getString("external_id"));

            assertEquals(expectedStation.getString("external_id"), storedStation.getString("external_id"));
            assertEquals(expectedStation.getString("name"), storedStation.getString("name"));
            assertEquals(expectedStation.getDouble("latitude"), storedStation.getDouble("latitude"), 0.01);
            assertEquals(expectedStation.getDouble("longitude"), storedStation.getDouble("longitude"), 0.01);
            assertEquals(expectedStation.getInt("altitude"), storedStation.getInt("altitude"));
        }
    }

    private JSONObject findStationByExternalId(String externalId) {
        for (int i = 0; i < storedStations.length(); i++) {
            JSONObject station = storedStations.getJSONObject(i);
            if (station.getString("external_id").equals(externalId)) {
                return station;
            }
        }
        return null;
    }
}
