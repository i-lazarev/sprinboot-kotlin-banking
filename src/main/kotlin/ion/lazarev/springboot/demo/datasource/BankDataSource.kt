package ion.lazarev.springboot.demo.datasource

import ion.lazarev.springboot.demo.model.Bank

interface BankDataSource {
    fun getBanks():Collection<Bank>
}