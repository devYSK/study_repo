package com.yscorp.ex1

import com.yscorp.ex1.domain.Client
import com.yscorp.ex1.entity.BankAccount
import com.yscorp.ex1.exception.ClientNotFoundException
import com.yscorp.ex1.repository.BankAccountRepo
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import javax.security.auth.login.AccountNotFoundException

@Service
class BankService(
    private val repo: BankAccountRepo,
) {


    private val log = KotlinLogging.logger {}

    var clients: List<Client> = listOf(
        Client(100L, "1", "John", "T.", "Doe"),
        Client(101L, "2", "Emma", "B.", "Smith"),
        Client(102L, "3", "James", "R.", "Brown"),
        Client(103L, "4", "Olivia", "S.", "Johnson"),
        Client(104L, "5", "William", "K.", "Jones")
    )


    fun save(account: BankAccount) {
        if (validClient(account)) repo.save(account)
        else throw ClientNotFoundException("Client Not Found")
    }

    fun modify(account: BankAccount): BankAccount {
        if (validClient(account)) repo.save(account)
        else throw ClientNotFoundException("Client Not Found")

        return account
    }

    fun getAccounts(): List<BankAccount> {
        return repo.findAll()
    }

    fun accountById(accountId: Long): BankAccount {
        if (repo.findById(accountId).isPresent) {
            return repo.findById(accountId).get()
        }
        throw AccountNotFoundException("Account Not Found")
    }

    fun delete(accountId: Long): Boolean {
        if (repo.findById(accountId).isPresent) {
            repo.delete(repo.findById(accountId).get())
            return true
        }
        return false
    }

    private fun getClients(): List<Client> {
        return clients
    }

    fun getBankAccountClientMap(bankAccounts: List<BankAccount>): Map<BankAccount, Client> {
        // Collect all client IDs from the list of bank accounts
        val clientIds = bankAccounts
            .map { it.clientId }

        // Fetch client for all collected IDs
        val clients = getClients()
            .filter { client: Client -> clientIds.contains(client.id) }

        // Map each bank account to its corresponding client
        return clients.associateBy { client ->
            bankAccounts.firstOrNull { bankAccount ->
                bankAccount.clientId == client.id
            }!!
        }

    }

    private fun validClient(account: BankAccount): Boolean {
        return getClients()
            .firstOrNull { client: Client -> client.id == account.clientId } != null
    }
}

