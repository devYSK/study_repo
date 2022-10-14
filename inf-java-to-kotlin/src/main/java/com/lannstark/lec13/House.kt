package com.lannstark.lec13

/**
 *
 * @author   : ysk
 */
class House (
    private var address: String,
    ) {
    private var livingRoom = this.LivingRoom(10.0)

    inner class LivingRoom(
        private var area: Double,
    ) {
        val address: String
            get() = this@House.address
    }

}
