package ion.lazarev.springboot.demo.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    val baseURL = "/api/banks"

    @Nested
    @DisplayName("getBanks()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            //when/then
            mockMvc.get(baseURL)
                .andDo { println() }
                .andExpect {
                    status { isOk() } // status 200
                    content { contentType(MediaType.APPLICATION_JSON) } // content-type json
                    jsonPath("$[0].accountNumber") { //checking first element accountNumber
                        value("B123")
                    }
                }
        }
    }

    @Nested
    @DisplayName("getBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            //given
            val accountNumber = "B234"

            //when / then
            mockMvc.get("$baseURL/$accountNumber")
                .andDo { println() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value(4.0) }
                    jsonPath("$.transactionFee") { value(15) }
                }


        }
    }

    @Test
    fun `should return NOT FOUNT if the account number does not exist`() {
        //given
        val accountNumber = "does_not_exists"

        //when / then
        mockMvc.get("$baseURL/$accountNumber")
            .andDo { print() }
            .andExpect { status { isNotFound() } }


    }


}