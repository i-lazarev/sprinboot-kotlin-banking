package ion.lazarev.springboot.demo.datasource.mock

import ion.lazarev.springboot.demo.datasource.BankDataSource
import ion.lazarev.springboot.demo.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {
    val banks = listOf(
        Bank("B123", 2.0, 1),
        Bank("B234", 4.0, 4),
        Bank("B345", 7.0, 3),
        Bank("B456", 12.0, 20)
    )

    override fun retrieveBanks(): Collection<Bank> = banks


}