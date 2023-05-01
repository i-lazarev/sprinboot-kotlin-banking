package ion.lazarev.springboot.demo.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ion.lazarev.springboot.demo.datasource.BankDataSource
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest {
    private val dataSource: BankDataSource = mockk(relaxed = true)
    private val bankService = BankService(dataSource)

    @Test
    fun `should call its source to retrieve banks`() {

        //when
        bankService.getBanks()

        //then
        // verify that the retrieve data source was actually called exactly once
        verify(exactly = 1) { dataSource.retrieveBanks() }
    }

}