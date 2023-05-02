package ion.lazarev.springboot.demo.datasource.mock

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MockBankDataSourceTest {
    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of banks`() {
        //when
        val banks = mockDataSource.retrieveBanks()

        //then
        Assertions.assertThat(banks.size).isGreaterThanOrEqualTo(3)

    }

    @Test
    fun `should provide some mock data`() {
        //when
        val banks = mockDataSource.retrieveBanks()

        //then
        Assertions.assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        Assertions.assertThat(banks).allMatch { it.trust != 0.0 }
        Assertions.assertThat(banks).allMatch { it.transactionFee != 0 }
        Assertions.assertThat(banks).allMatch { bank -> banks.count { it.accountNumber == bank.accountNumber } == 1 }

    }
}