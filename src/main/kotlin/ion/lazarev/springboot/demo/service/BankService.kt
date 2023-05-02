package ion.lazarev.springboot.demo.service

import ion.lazarev.springboot.demo.datasource.BankDataSource
import ion.lazarev.springboot.demo.model.Bank
import org.springframework.stereotype.Service

@Service
class BankService(private val dataSource: BankDataSource) {
    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()

    fun getBank(accountNumber: String): Bank = dataSource.retrieveBank(accountNumber)

    fun addBank(newBank:Bank): Bank= dataSource.postBank(newBank)
}