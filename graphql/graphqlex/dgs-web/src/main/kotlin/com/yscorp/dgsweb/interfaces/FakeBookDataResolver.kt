package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.yscorp.dgsweb.codegen.DgsConstants
import com.yscorp.dgsweb.codegen.types.Book
import com.yscorp.dgsweb.codegen.types.ReleaseHistory
import com.yscorp.dgsweb.codegen.types.ReleaseHistoryInput
import com.yscorp.dgsweb.fake.FakeBookDataSource
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class FakeBookDataResolver {

    @DgsQuery(field = "books")
    fun booksWrittenBy(@InputArgument(name = "author") authorName: String?): List<Book> {
        return if (authorName.isNullOrBlank()) {
            FakeBookDataSource.BOOK_LIST
        } else {
            FakeBookDataSource.BOOK_LIST.filter { book ->
                book.author.name.contains(authorName, ignoreCase = true)
            }
        }
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.BooksByReleased)
    fun getBooksByReleased(dataFetchingEnvironment: DataFetchingEnvironment): List<Book> {

        val releasedMap = dataFetchingEnvironment.getArgument<Map<String, Any>>("releasedInput")

        val releasedInput = ReleaseHistoryInput(
            printedEdition = releasedMap?.get(DgsConstants.RELEASEHISTORYINPUT.PrintedEdition) as Boolean,
            year = releasedMap[DgsConstants.RELEASEHISTORYINPUT.Year] as Int
        )

        return FakeBookDataSource.BOOK_LIST.filter { book ->
            matchReleaseHistory(releasedInput, book.released ?: return@filter false)
        }
    }

    private fun matchReleaseHistory(input: ReleaseHistoryInput, element: ReleaseHistory): Boolean {
        return input.printedEdition == element.printedEdition && input.year == element.year
    }

}