package ion.lazarev.springboot.demo.datasource

import ion.lazarev.springboot.demo.model.Bank

interface BankDataSource {
    fun retrieveBanks(): Collection<Bank>

    fun retrieveBank(accountNumber: String): Bank

    fun postBank(newBank: Bank): Bank

    fun patchBank(data: Bank): Bank

    fun deleteBank(accountNumber: String)
}