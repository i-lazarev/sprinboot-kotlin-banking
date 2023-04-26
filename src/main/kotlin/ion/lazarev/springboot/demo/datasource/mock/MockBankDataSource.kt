package ion.lazarev.springboot.demo.datasource.mock

import ion.lazarev.springboot.demo.datasource.BankDataSource
import ion.lazarev.springboot.demo.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {
    val banks = listOf(
        Bank("B123",0.0,1),
        Bank("B234",0.0,2),
        Bank("B345",0.0,3)
    )
    override fun getBanks(): Collection<Bank> = banks


}