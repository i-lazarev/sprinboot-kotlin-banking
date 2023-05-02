package ion.lazarev.springboot.demo.datasource.mock

import ion.lazarev.springboot.demo.datasource.BankDataSource
import ion.lazarev.springboot.demo.model.Bank
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository
class MockBankDataSource : BankDataSource {
    val banks = mutableListOf(
        Bank("B123", 2.0, 1),
        Bank("B234", 4.0, 15),
        Bank("B345", 7.0, 3),
        Bank("B456", 12.0, 20)
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank = banks.firstOrNull { it.accountNumber == accountNumber }
        ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")

    override fun postBank(newBank: Bank): Bank {
        if (banks.any { it.accountNumber == newBank.accountNumber }){
            throw IllegalArgumentException("Bank with account number ${newBank.accountNumber} already exists!")
        }
            banks.add(newBank)
        return newBank
    }

}