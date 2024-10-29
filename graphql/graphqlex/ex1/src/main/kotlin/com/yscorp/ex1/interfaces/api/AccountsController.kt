package com.yscorp.ex1.interfaces.api


import com.yscorp.ex1.BankService
import com.yscorp.ex1.config.JwtUtils
import com.yscorp.ex1.domain.AuthPayload
import com.yscorp.ex1.domain.Client
import com.yscorp.ex1.domain.User
import com.yscorp.ex1.entity.BankAccount
import graphql.GraphQLError
import graphql.schema.DataFetchingEnvironment
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.graphql.data.method.annotation.*
import org.springframework.graphql.execution.ErrorType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller


@Controller
class AccountsController(
    val jwUtils: JwtUtils,
    val bankService: BankService,

    ) {

    private val log = KotlinLogging.logger { }

    @Value("\${app.jwt.secret}")
    private val jwtSecret: String? = null

    @Value("\${app.systemUser.login.user}")
    private val systemUser: String? = null

    @Value("\${app.systemUser.login.password}")
    private val systemPassword: String? = null

    @QueryMapping
    fun accounts(): List<BankAccount> {
        val authentication = SecurityContextHolder.getContext().authentication
        log.info("Is User Authenticated : " + authentication.isAuthenticated)
        return bankService.getAccounts()
    }

    @QueryMapping(name = "login")
    fun loginQuery(@Argument email: String, @Argument password: String): AuthPayload {
        /*No authentication required as this is a public query*/
        if (systemUser == email && systemPassword == password) {
            val payload: AuthPayload = AuthPayload(jwUtils.generateJWTToken(), User("Login User", email, email))
            return payload
        } else {
            throw RuntimeException("Invalid credentials")
        }
    }
//
//    @SchemaMapping(typeName = "BankAccount", field = "client")
//    fun getClient(account: BankAccount): Client? {
//        val authentication = SecurityContextHolder.getContext().authentication
//        log.info(("Getting client for " + account.id).toString() + " for user: " + authentication.name)
//        return bankService.getClientByAccountId(account.id)
//    }

    @QueryMapping
    fun accountById(@Argument("accountId") accountId: Long?): BankAccount {
        log.info("Getting Account ")
        return bankService.accountById(accountId!!)
    }

    @BatchMapping(field = "client", typeName = "BankAccountType")
    fun clients(bankAccounts: List<BankAccount>): Map<BankAccount, Client> {
        log.info("Getting client for Accounts : " + bankAccounts.size)
        return bankService.getBankAccountClientMap(bankAccounts)
    }

    @MutationMapping
    fun addAccount(@Argument("account") account: BankAccount): Boolean {
        log.info("Saving Account : $account")
        bankService.save(account)
        return true
    }

    @MutationMapping
    fun editAccount(@Argument("account") account: BankAccount): BankAccount {
        log.info("Editing Account : $account")
        return bankService.modify(account)
    }

    @MutationMapping
    fun deleteAccount(@Argument("id") accountId: Long): Boolean {
        log.info("Deleting Account : $accountId")
        return bankService.delete(accountId)
    }
    @GraphQlExceptionHandler
    fun handle(ex: Exception, environment: DataFetchingEnvironment): GraphQLError {
        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(ex.message)
            .path(environment.executionStepInfo.path)
            .location(environment.field.sourceLocation)
            .build()
    }
}
