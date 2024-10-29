package com.yscorp.dgsweb.domain

import com.yscorp.dgsweb.codegen.types.UniqueCustomerInput
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*


@Service
class CustomerQueryService(
    private val customerRepository: CustomerRepository,
) {

    fun findUniqueCustomer(uniqueCustomerInput: UniqueCustomerInput): Customer? {
        val customerUuid = uniqueCustomerInput.uuid
        val customerEmail = uniqueCustomerInput.email

        require(!(customerUuid.isNullOrBlank() && customerEmail.isNullOrBlank())) {
            "One of customer UUID OR customer email must exist"
        }

        require(customerUuid.isNullOrBlank() || customerEmail.isNullOrBlank()) {
            "Only one parameter (customer UUID OR customer email) allowed, not both"
        }

        val specification = when {
            customerUuid!!.isNotBlank() -> Specification.where(CustomerSpecification.uuidEqualsIgnoreCase(customerUuid!!))
            else -> Specification.where(CustomerSpecification.emailEqualsIgnoreCase(customerEmail!!))
        }

        return customerRepository.findOne(specification).orElse(null)
    }

    fun findCustomers(
        customer: UniqueCustomerInput?,
        page: Int? = 0,
        size: Int? = 50,
    ): Page<Customer> {
        val pageable = PageRequest.of(
            page ?: 0,
            size ?: 50,
            Sort.by(CustomerSpecification.FIELD_EMAIL)
        )

        return customer?.let {
            val existingCustomer = findUniqueCustomer(it)

            if (existingCustomer != null) {
                PageImpl(listOf(existingCustomer), pageable, 1)
            } else {
                PageImpl(emptyList(), pageable, 0)
            }
        } ?: customerRepository.findAll(pageable)
    }
}


object CustomerSpecification : BaseSpecification() {
    const val FIELD_UUID: String = "uuid"
    const val FIELD_EMAIL: String = "email"

    fun uuidEqualsIgnoreCase(keyword: String?): Specification<Customer> {
        return Specification { root: Root<Customer>, _, criteriaBuilder: CriteriaBuilder ->
            keyword?.let {
                criteriaBuilder.equal(
                    root.get<UUID>(FIELD_UUID),  // 타입을 명시
                    UUID.fromString(it.lowercase(Locale.getDefault()))
                )
            } ?: criteriaBuilder.conjunction() // keyword가 null인 경우 빈 조건 반환
        }
    }

    fun emailEqualsIgnoreCase(keyword: String?): Specification<Customer> {
        return Specification { root: Root<Customer>, _, criteriaBuilder: CriteriaBuilder ->
            keyword?.let {
                criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get<String>(FIELD_EMAIL)), // 타입을 명시
                    it.lowercase(Locale.getDefault())
                )
            } ?: criteriaBuilder.conjunction() // keyword가 null인 경우 빈 조건 반환
        }
    }
}

abstract class BaseSpecification {
    companion object {
        protected fun contains(keyword: String?): String {
            return "%${keyword ?: ""}%"
        }
    }
}