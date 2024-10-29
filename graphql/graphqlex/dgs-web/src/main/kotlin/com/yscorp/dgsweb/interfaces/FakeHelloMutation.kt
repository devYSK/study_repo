package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.yscorp.dgsweb.codegen.DgsConstants
import com.yscorp.dgsweb.codegen.types.Hello
import com.yscorp.dgsweb.codegen.types.HelloInput
import com.yscorp.dgsweb.fake.DataFakerConfig


@DgsComponent
class FakeHelloMutation {
    //    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.AddHello)
    @DgsMutation
    fun addHello(@InputArgument(name = "helloInput") helloInput: HelloInput): Int {
        val hello = Hello(
            { helloInput.text },
            { helloInput.number }
        )

        DataFakerConfig.HELLO_LIST.add(hello)

        return DataFakerConfig.HELLO_LIST.size
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.ReplaceHelloText)
    fun replaceHelloText(@InputArgument(name = "helloInput") helloInput: HelloInput): List<Hello> {
        val copiedList = DataFakerConfig.HELLO_LIST.toList()


        copiedList.filter { h -> h.randomNumber === helloInput.number }.forEach { h ->
            Hello(
                { helloInput.text },
                { helloInput.number }
            )
        }

        return copiedList
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.DeleteHello)
    fun deleteHello(number: Int): Int {
        DataFakerConfig.HELLO_LIST.removeIf { h -> h.randomNumber === number }

        return DataFakerConfig.HELLO_LIST.size
    }
}