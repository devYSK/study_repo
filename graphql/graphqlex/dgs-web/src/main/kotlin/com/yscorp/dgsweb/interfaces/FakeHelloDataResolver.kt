package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.yscorp.dgsweb.fake.DataFakerConfig
import com.yscorp.dgsweb.codegen.types.Hello

@DgsComponent
class FakeHelloDataResolver {

    @DgsQuery
    fun allHellos(): List<Hello> {
        return DataFakerConfig.HELLO_LIST
    }

    @DgsQuery
    fun oneHello(): Hello {
        return DataFakerConfig.HELLO_LIST.random()
    }

}