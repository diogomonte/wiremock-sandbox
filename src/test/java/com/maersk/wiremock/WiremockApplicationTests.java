package com.maersk.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.recording.RecordSpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.RouteMatcher;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.recordSpec;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class WiremockApplicationTests {

	static WireMockServer wireMockServer = new WireMockServer(8081);

	@Autowired
	Router router;

	@Autowired
	EnergyApi energyApi;

	@BeforeAll
	static void beforeAll() {
		wireMockServer.start();
		wireMockServer.startRecording(recordSpec()
				.forTarget("http://localhost:8081")
				.captureHeader("Accept")
				.captureHeader("Content-Type", true)
				.ignoreRepeatRequests());
		configureFor("localhost", 8081);
	}

	@AfterAll
	static void afterAll() {
		wireMockServer.stopRecording();
		wireMockServer.stop();
	}

	@Test
	void testProxy() {
		wireMockServer.stubFor(get(urlEqualTo("/dataset/Elspotprices"))
				.willReturn(aResponse()
						.proxiedFrom("https://api.energidataservice.dk")));


		WebTestClient.bindToRouterFunction(router.data(energyApi))
				.build()
				.get()
				.uri("/data")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(EnergyData.class)
				.consumeWith(res -> {
					final var body = res.getResponseBody();
					assertEquals(100, body.records.size());
				});

	}

	@Test
	void testHardcodedBody() {
		wireMockServer.stubFor(any(anyUrl())
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody("{\n" +
								"    \"records\": [\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"108.500000\",\n" +
								"            \"SpotPriceEUR\": \"14.570000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"0.740000\",\n" +
								"            \"SpotPriceEUR\": \"0.100000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"111.779999\",\n" +
								"            \"SpotPriceEUR\": \"15.010000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"0.740000\",\n" +
								"            \"SpotPriceEUR\": \"0.100000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"0.740000\",\n" +
								"            \"SpotPriceEUR\": \"0.100000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"14.220000\",\n" +
								"            \"SpotPriceEUR\": \"1.910000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"212.020004\",\n" +
								"            \"SpotPriceEUR\": \"28.469999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"130.919998\",\n" +
								"            \"SpotPriceEUR\": \"17.580000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"218.350006\",\n" +
								"            \"SpotPriceEUR\": \"29.320000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"130.919998\",\n" +
								"            \"SpotPriceEUR\": \"17.580000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"130.919998\",\n" +
								"            \"SpotPriceEUR\": \"17.580000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"36.939999\",\n" +
								"            \"SpotPriceEUR\": \"4.960000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"361.399994\",\n" +
								"            \"SpotPriceEUR\": \"48.529999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"361.399994\",\n" +
								"            \"SpotPriceEUR\": \"48.529999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"372.200012\",\n" +
								"            \"SpotPriceEUR\": \"49.980000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"361.399994\",\n" +
								"            \"SpotPriceEUR\": \"48.529999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"361.399994\",\n" +
								"            \"SpotPriceEUR\": \"48.529999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"65.910004\",\n" +
								"            \"SpotPriceEUR\": \"8.850000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"422.839996\",\n" +
								"            \"SpotPriceEUR\": \"56.779999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"422.839996\",\n" +
								"            \"SpotPriceEUR\": \"56.779999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"422.839996\",\n" +
								"            \"SpotPriceEUR\": \"56.779999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"422.839996\",\n" +
								"            \"SpotPriceEUR\": \"56.779999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"422.839996\",\n" +
								"            \"SpotPriceEUR\": \"56.779999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"111.110001\",\n" +
								"            \"SpotPriceEUR\": \"14.920000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"441.829987\",\n" +
								"            \"SpotPriceEUR\": \"59.330002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"441.829987\",\n" +
								"            \"SpotPriceEUR\": \"59.330002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"441.829987\",\n" +
								"            \"SpotPriceEUR\": \"59.330002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"441.829987\",\n" +
								"            \"SpotPriceEUR\": \"59.330002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"441.829987\",\n" +
								"            \"SpotPriceEUR\": \"59.330002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"148.339996\",\n" +
								"            \"SpotPriceEUR\": \"19.920000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"168.529999\",\n" +
								"            \"SpotPriceEUR\": \"22.629999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"445.260010\",\n" +
								"            \"SpotPriceEUR\": \"59.790001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"174.110001\",\n" +
								"            \"SpotPriceEUR\": \"23.379999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"307.040009\",\n" +
								"            \"SpotPriceEUR\": \"41.230000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"307.040009\",\n" +
								"            \"SpotPriceEUR\": \"41.230000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"311.959991\",\n" +
								"            \"SpotPriceEUR\": \"41.889999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"307.040009\",\n" +
								"            \"SpotPriceEUR\": \"41.230000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"307.040009\",\n" +
								"            \"SpotPriceEUR\": \"41.230000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"120.940002\",\n" +
								"            \"SpotPriceEUR\": \"16.240000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"93.760002\",\n" +
								"            \"SpotPriceEUR\": \"12.590000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"91.519997\",\n" +
								"            \"SpotPriceEUR\": \"12.290000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"96.589996\",\n" +
								"            \"SpotPriceEUR\": \"12.970000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"91.519997\",\n" +
								"            \"SpotPriceEUR\": \"12.290000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"91.519997\",\n" +
								"            \"SpotPriceEUR\": \"12.290000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"80.800003\",\n" +
								"            \"SpotPriceEUR\": \"10.850000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"148.869995\",\n" +
								"            \"SpotPriceEUR\": \"19.990000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"151.169998\",\n" +
								"            \"SpotPriceEUR\": \"20.299999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"153.330002\",\n" +
								"            \"SpotPriceEUR\": \"20.590000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"151.169998\",\n" +
								"            \"SpotPriceEUR\": \"20.299999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"151.169998\",\n" +
								"            \"SpotPriceEUR\": \"20.299999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"111.779999\",\n" +
								"            \"SpotPriceEUR\": \"15.010000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"291.850006\",\n" +
								"            \"SpotPriceEUR\": \"39.189999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"291.850006\",\n" +
								"            \"SpotPriceEUR\": \"39.189999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"300.559998\",\n" +
								"            \"SpotPriceEUR\": \"40.360001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"291.850006\",\n" +
								"            \"SpotPriceEUR\": \"39.189999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"291.850006\",\n" +
								"            \"SpotPriceEUR\": \"39.189999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"158.550003\",\n" +
								"            \"SpotPriceEUR\": \"21.290001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"370.709991\",\n" +
								"            \"SpotPriceEUR\": \"49.779999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"381.809998\",\n" +
								"            \"SpotPriceEUR\": \"51.270000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"381.809998\",\n" +
								"            \"SpotPriceEUR\": \"51.270000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"381.809998\",\n" +
								"            \"SpotPriceEUR\": \"51.270000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"381.809998\",\n" +
								"            \"SpotPriceEUR\": \"51.270000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"178.130005\",\n" +
								"            \"SpotPriceEUR\": \"23.920000\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"459.109985\",\n" +
								"            \"SpotPriceEUR\": \"61.650002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"459.109985\",\n" +
								"            \"SpotPriceEUR\": \"61.650002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"459.109985\",\n" +
								"            \"SpotPriceEUR\": \"61.650002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"459.109985\",\n" +
								"            \"SpotPriceEUR\": \"61.650002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"459.109985\",\n" +
								"            \"SpotPriceEUR\": \"61.650002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"257.220001\",\n" +
								"            \"SpotPriceEUR\": \"34.540001\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"531.869995\",\n" +
								"            \"SpotPriceEUR\": \"71.419998\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"531.869995\",\n" +
								"            \"SpotPriceEUR\": \"71.419998\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"531.869995\",\n" +
								"            \"SpotPriceEUR\": \"71.419998\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"531.869995\",\n" +
								"            \"SpotPriceEUR\": \"71.419998\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"531.869995\",\n" +
								"            \"SpotPriceEUR\": \"71.419998\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"383.739990\",\n" +
								"            \"SpotPriceEUR\": \"51.529999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"547.429993\",\n" +
								"            \"SpotPriceEUR\": \"73.510002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"547.429993\",\n" +
								"            \"SpotPriceEUR\": \"73.510002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"547.429993\",\n" +
								"            \"SpotPriceEUR\": \"73.510002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"547.429993\",\n" +
								"            \"SpotPriceEUR\": \"73.510002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"547.429993\",\n" +
								"            \"SpotPriceEUR\": \"73.510002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"427.380005\",\n" +
								"            \"SpotPriceEUR\": \"57.389999\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"573.500000\",\n" +
								"            \"SpotPriceEUR\": \"77.010002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"573.500000\",\n" +
								"            \"SpotPriceEUR\": \"77.010002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"573.500000\",\n" +
								"            \"SpotPriceEUR\": \"77.010002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"573.500000\",\n" +
								"            \"SpotPriceEUR\": \"77.010002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE4\",\n" +
								"            \"SpotPriceDKK\": \"573.500000\",\n" +
								"            \"SpotPriceEUR\": \"77.010002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SYSTEM\",\n" +
								"            \"SpotPriceDKK\": \"475.339996\",\n" +
								"            \"SpotPriceEUR\": \"63.830002\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK1\",\n" +
								"            \"SpotPriceDKK\": \"574.090027\",\n" +
								"            \"SpotPriceEUR\": \"77.089996\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"DK2\",\n" +
								"            \"SpotPriceDKK\": \"574.090027\",\n" +
								"            \"SpotPriceEUR\": \"77.089996\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"NO2\",\n" +
								"            \"SpotPriceDKK\": \"574.090027\",\n" +
								"            \"SpotPriceEUR\": \"77.089996\"\n" +
								"        },\n" +
								"        {\n" +
								"            \"HourDKM\": null,\n" +
								"            \"PriceArea\": \"SE3\",\n" +
								"            \"SpotPriceDKK\": \"574.090027\",\n" +
								"            \"SpotPriceEUR\": \"77.089996\"\n" +
								"        }\n" +
								"    ]\n" +
								"}")));


		WebTestClient.bindToRouterFunction(router.data(energyApi))
				.build()
				.get()
				.uri("/data")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(EnergyData.class)
				.consumeWith(res -> {
					final var body = res.getResponseBody();
					assertEquals(100, body.records.size());
				});

	}

}
