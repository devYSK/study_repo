package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.*
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.yscorp.dgsweb.codegen.DgsConstants
import com.yscorp.dgsweb.codegen.types.*
import com.yscorp.dgsweb.domain.CustomerCommandService
import com.yscorp.dgsweb.domain.CustomerQueryService
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironment
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@DgsComponent
class CustomerResolver(
    private val customerCommandService: CustomerCommandService,
    private val customerQueryService: CustomerQueryService,
) {

    @DgsMutation
    fun addNewCustomer(
        @InputArgument customer: AddCustomerInput
    ): CustomerMutationResponse {
        val saved = customerCommandService.addNewCustomer(customer)

        return CustomerMutationResponse(
            customerUuid = { saved.uuid.toString() },
            success = { true },
            message = { "Customer ${customer.fullName} saved" }
        )
    }

    @DgsMutation
    fun addAddressesToExistingCustomer(
        @InputArgument customer: UniqueCustomerInput,
        @InputArgument addresses: List<AddAddressInput>,
        env: DgsDataFetchingEnvironment
    ): CustomerMutationResponse {
        val existingCustomer = customerQueryService.findUniqueCustomer(customer)
            ?: throw DgsEntityNotFoundException(
                "Customer: uuid ${customer.uuid} / email ${customer.email} not found"
            )

        customerCommandService.addAddressesToExistingCustomer(existingCustomer, addresses)

        return CustomerMutationResponse(
            customerUuid = { existingCustomer.uuid.toString() },
            success = { true },
            message = { "Added ${addresses.size} addresses to customer" }
        )
    }

    @DgsQuery
    fun customerPagination(
        @InputArgument customer: UniqueCustomerInput?,
        env: DataFetchingEnvironment,
        @InputArgument page: Int,
        @InputArgument size: Int
    ): CustomerPagination {
        val pageCustomer = customerQueryService.findCustomers(customer, page, size)
        val listCustomerAsGraphql = pageCustomer.content?.map {

            Customer(
                uuid = { it.uuid.toString() },
                fullName = { it.fullName },
                email = { it.email },
                phone = { it.phone },
                birthDate = { it.birthDate },
                addresses = {
                    it.addresses?.map { address ->
                        Address(
                            street = { address.street },
                            city = { address.city },
                            zipCode = { address.zipcode }
                        )
                    }
                }
            )

//            CustomerMapper.mapToGraphqlEntity(it)
        } ?: emptyList()

        val pageConnection = SimpleListConnection(listCustomerAsGraphql).get(env)

        return CustomerPagination(
            customerConnection = { pageConnection },
            page = { pageCustomer.number },
            size = { pageCustomer.size },
            totalElement = { pageCustomer.totalElements },
            totalPage = { pageCustomer.totalPages }
        )
    }

    @DgsMutation
    fun addDocumentToExistingCustomer(
        @InputArgument customer: UniqueCustomerInput,
        @InputArgument documentType: String,
        env: DataFetchingEnvironment
    ): CustomerMutationResponse {
        val existingCustomer = customerQueryService.findUniqueCustomer(customer)
            ?: throw DgsEntityNotFoundException(
                "Customer: uuid ${customer.uuid} / email ${customer.email} not found"
            )

        val documentFile = env.getArgument<MultipartFile>(
            DgsConstants.MUTATION.ADDDOCUMENTTOEXISTINGCUSTOMER_INPUT_ARGUMENT.DocumentFile
        ) ?: throw IllegalArgumentException("Document file is required")

        customerCommandService.addDocumentToExistingCustomer(existingCustomer, documentType, documentFile)

        return CustomerMutationResponse(
            customerUuid = { existingCustomer.uuid.toString() },
            success = { true },
            message = { "${documentFile?.originalFilename} uploaded" }
        )
    }

//    @DgsMutation
//    fun uploadFile(dfe: DataFetchingEnvironment): String {
//        val file: MultipartFile? = dfe.getArgument("file")
//
//        val uploadDir = Paths.get("uploads")
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir)
//        }
//
//        val filePath = uploadDir.resolve(file.originalFilename!!)
//        file.transferTo(filePath.toFile())
//
//        return "파일 업로드 성공: ${file.originalFilename}"
//    }
}
