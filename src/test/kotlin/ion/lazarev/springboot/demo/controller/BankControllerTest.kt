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
    @TestInstance(Lifecycle.PER_CLASS)
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
    @TestInstance(Lifecycle.PER_CLASS)
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
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newBank))
                    }

//                    jsonPath("$.accountNumber") { value("B45678") }
//                    jsonPath("$.trust") { value(31.23) }
//                    jsonPath("$.transactionFee") { value(2) }
                }
            mockMvc.get("$baseURL/${newBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(newBank)) } }

        }

        @Test
        fun `should return BAD REQUEST if bank with given accountNumber already exists`() {
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

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an existing bank`() {
            //given
            val updateBank = Bank("B123", 2.0, 1)

            //when
            val performPatchRequest = mockMvc.patch(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBank)

            }

            //then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updateBank))
                    }
                }
            mockMvc.get("$baseURL/${updateBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updateBank)) } }
        }

        @Test
        fun `should return BAD REQUEST if no bank with given account number exists`() {
            val invalidBank = Bank("B1236789", 2.6, 4)

            //when
            val performPatch = mockMvc.patch(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            //then
            performPatch
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteBank {
        @Test
        fun `should delete the bank with the given account number`() {
            //given
            val accountNumber = "B123"

            //when / then
            mockMvc.delete("$baseURL/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }
            mockMvc.get("$baseURL/$accountNumber")
                .andExpect { status { isNotFound() } }
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
}