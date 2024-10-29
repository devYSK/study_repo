package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.InputArgument
import com.yscorp.dgsweb.codegen.DgsConstants
import com.yscorp.dgsweb.codegen.types.MobileApp
import com.yscorp.dgsweb.codegen.types.MobileAppFilter
import com.yscorp.dgsweb.fake.FakeMobileAppDataSource
import java.time.LocalDate

@DgsComponent
class FakeMobileAppDataResolver {

    @DgsData(
        parentType = DgsConstants.QUERY_TYPE,
        field = DgsConstants.QUERY.MobileApps
    )
    fun getMobileApps(
        @InputArgument(name = "mobileAppFilter") filter: MobileAppFilter?,
    ): List<MobileApp> {
        val mobileAppList = FakeMobileAppDataSource.MOBILE_APP_LIST

        return filter?.let {
            mobileAppList.filter { mobileApp -> matchFilter(it, mobileApp) }
        } ?: mobileAppList
    }

    private fun matchFilter(mobileAppFilter: MobileAppFilter, mobileApp: MobileApp): Boolean {
        val isAppMatch = mobileApp.name.contains(mobileAppFilter.name.orEmpty(), ignoreCase = true) &&
            mobileApp.version?.contains(mobileAppFilter.version.orEmpty(), ignoreCase = true) ?: false &&
            mobileApp.releaseDate?.isAfter(mobileAppFilter.releasedAfter ?: LocalDate.MIN) ?: false &&
            (mobileApp.downloaded ?: 0) >= (mobileAppFilter.minimumDownload ?: 0)

        if (!isAppMatch) return false

        if (!mobileAppFilter.platform.isNullOrBlank() &&
            mobileApp.platform?.contains(mobileAppFilter.platform!!.lowercase()) != true
        ) {
            return false
        }

        if (mobileAppFilter.author != null && mobileApp.author?.name?.contains(
                mobileAppFilter.author?.name.orEmpty(),
                ignoreCase = true) != true
        ) {
            return false
        }

        return true
    }
}
