package com.yscorp.reactiver2dbc.domain

import com.yscorp.reactiver2dbc.dto.CustomerDto
import com.yscorp.reactiver2dbc.dto.DeleteResponseDto
import com.yscorp.reactiver2dbc.dto.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service


@Service
class CustomerService(
    private val repository: CustomerRepository,
) {

//    fun allCustomers(): Flow<CustomerDto> {
//        return repository.findAll()
//            .map { CustomerDto(it) }
//    }

    fun allCustomers(): Flow<Customer> {
        return repository.findAll()
    }

    suspend fun customerById(id: Int): CustomerDto? {
        return repository.findById(id)?.let {
            CustomerDto(it)
        }
    }

    suspend fun createCustomer(dto: CustomerDto): CustomerDto {
        return CustomerDto(repository.save(dto.toEntity()))
    }

    suspend fun updateCustomer(id: Int, dto: CustomerDto): CustomerDto {
        val customer = repository.findById(id) ?: throw IllegalArgumentException("Customer not found")

        // 고객 정보 업데이트
        customer.name = dto.name
        customer.age = dto.age
        customer.city = dto.city

        // 저장 후 CustomerDto로 변환하여 반환
        return CustomerDto(repository.save(customer))

    }

    suspend fun deleteCustomer(id: Int): DeleteResponseDto {
        repository.deleteById(id)

        return DeleteResponseDto(id, Status.SUCCESS)
    }

}
