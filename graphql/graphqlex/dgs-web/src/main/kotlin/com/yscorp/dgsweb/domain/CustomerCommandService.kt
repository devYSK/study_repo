package com.yscorp.dgsweb.domain

import com.yscorp.dgsweb.codegen.types.AddAddressInput
import com.yscorp.dgsweb.codegen.types.AddCustomerInput
import com.yscorp.dgsweb.codegen.types.UpdateCustomerInput
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class CustomerCommandService(
    private val customerRepository: CustomerRepository
) {

    fun addNewCustomer(addCustomerInput: AddCustomerInput): Customer {

        val customerEntity = Customer(
             fullName = addCustomerInput.fullName,
             email = addCustomerInput.email,
             phone = addCustomerInput.phone,
             birthDate = addCustomerInput.birthDate,
             addresses = addCustomerInput.addresses?.map {
                Address(
                    uuid = UUID.randomUUID(),
                    street = it.street ,
                    city = it.city,
                    zipcode = it.zipcode
                )
            }?.toMutableList() ?: mutableListOf(),
            documents = mutableListOf(),
            uuid = UUID.randomUUID(),
        )

        return customerRepository.save(customerEntity)
    }

    fun addAddressesToExistingCustomer(
        customer: Customer,
        addressList: List<AddAddressInput>
    ): Customer {
        val addressesEntity = addressList.map {
            Address(
                uuid = UUID.randomUUID(),
                street = it.street ,
                city = it.city,
                zipcode = it.zipcode
            )
        }
        customer.addresses.addAll(addressesEntity)
        return customerRepository.save(customer)
    }

    fun addDocumentToExistingCustomer(
        customer: Customer,
        documentType: String,
        documentFile: MultipartFile
    ): Customer {
        val documentEntity = CustomerDocument().apply {
            this.documentType = documentType
            this.documentPath = "https://dummy-storage.com/${customer.uuid}/$documentType-${
                UUID.randomUUID().toString()
            }-${documentFile.originalFilename}"
        }

        customer.documents.add(documentEntity)
        return customerRepository.save(customer)
    }

    fun updateCustomer(customer: Customer, customerUpdate: UpdateCustomerInput): Customer {
        customerUpdate.email?.takeIf { it.isNotBlank() }?.let {
            customer.email = it
        }

        customerUpdate.phone?.takeIf { it.isNotBlank() }?.let {
            customer.phone = it
        }

        return customerRepository.save(customer)
    }
}
