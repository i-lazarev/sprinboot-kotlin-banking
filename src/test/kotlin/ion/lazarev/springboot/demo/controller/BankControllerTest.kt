package ion.lazarev.springboot.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import ion.lazarev.springboot.demo.model.Bank
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.*


@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc, val objectMapper: ObjectMapper
) {

    val baseURL = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            //when/then
            mockMvc.get(baseURL).andDo { println() }.andExpect {
                status { isOk() } // status 200
                content { contentType(MediaType.APPLICATION_JSON) } // content-type json
                jsonPath("$[0].accountNumber") { //checking first element accountNumber
                    value("B123")
                }
            }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            //given
            val accountNumber = "B234"

            //when / then
            mockMvc.get("$baseURL/$accountNumber").andDo { println() }.andExpect {
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

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add the new bank`() {
            //given
            val newBank = Bank("B45678", 31.23, 2)

            //when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.accountNumber"){value("B45678")}
                    jsonPath("$.trust"){value(31.23)}
                    jsonPath("$.transactionFee"){value(2)}
                }

        }

        @Test
        fun `should return BAD REQUEST if bank with given accountNumber already exists`(){
            //given
            val invalidBank = Bank("B123", 2.6, 4)

            //when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            //then
            performPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }

         }


    }


}