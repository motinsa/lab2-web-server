package es.unizar.webeng.lab2

import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContexts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.security.cert.X509Certificate


/**
 * Integration test for the class LAB2
 *
 * Collection of integration tests to check the operation of the server.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IntegrationTest {

    /**
     * The annotation [LocalServerPort] tells the test runner that
     * it must inject in [port] the HTTP port that got allocated at runtime.
     * Useful for building URI when the [web environment][SpringBootTest.webEnvironment]
     * is set to use a [random port][WebEnvironment.RANDOM_PORT].
     */
    @LocalServerPort
    private var port: Int = 0

    /**
     * The annotation [Autowired] happens by placing an instance of one bean into the desired
     * field in an instance of another bean. Both classes should be beans, i.e. they should be
     * defined to live in the application context. TestRestTemplate is used to send http request in our
     * SpringBootTests and has all necessary methods to send the request to server.
     */
    @Autowired
    private lateinit var restTemplate: TestRestTemplate


    /**
     * Tests that creates a modified Apache HTTP client that connects to the server address and checks that the code of
     * status and the body returned by the server is correct.
     */
    /**
     * Skip SSL certificate verification in Spring Rest Template
     */
    fun skipSSL(){

        val acceptingTrustStrategy =
            TrustStrategy { chain: Array<X509Certificate?>?, authType: String? -> true }

        val sslContext = SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build()

        val csf = SSLConnectionSocketFactory(sslContext)

        val httpClient = HttpClients.custom()
            .setSSLSocketFactory(csf)
            .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory()

        requestFactory.httpClient = httpClient

        val restTemplate = RestTemplate(requestFactory)
    }
    @Test
    fun testHome() {

        with(restTemplate.getForEntity("https://localhost:$port", String::class.java)) {

            skipSSL()

            /**
             * The method assertThat is one of the JUnit methods from the Assert object that can be used to check if a
             * specific value match to an expected one. It primarily accepts 2 parameters.
             * First one if the actual value and the second is a matcher object.
             */
            assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        }
    }
    @Test
    fun testTime(){
        with(restTemplate.getForEntity("https://localhost:$port/time", String::class.java)) {

            skipSSL()

            /**
             * The method assertThat is one of the JUnit methods from the Assert object that can be used to check if a
             * specific value match to an expected one. It primarily accepts 2 parameters.
             * First one if the actual value and the second is a matcher object.
             */
            assertThat(statusCode).isEqualTo(HttpStatus.OK)

        }


    }

}
