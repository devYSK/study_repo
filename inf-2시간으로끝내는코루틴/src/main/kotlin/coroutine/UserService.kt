package coroutine

import jdk.internal.org.jline.utils.Colors.s
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext

class UserService {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    private abstract class FindUserContinuation(val userId: Long) : Continuation {
        var label = 0
        var profile: Profile? = null
        var image: Image? = null
    }

    suspend fun findUser(userId: Long, continuation: Continuation?): UserDto {
        val sm = continuation as? FindUserContinuation ?: object : FindUserContinuation(
            userId
        ) {
            override suspend fun resumeWith(data: Any?) {
                when (super.label) {
                    0 -> {
                        profile = data as Profile
                        label = 1
                    }

                    1 -> {
                        image = data as Image
                        label = 2
                    }
                }
                findUser(userId, this)
            }
        }

        when (sm.label) {
            0 -> {
                println("프로필을 가져오겠습니다")
                userProfileRepository.findProfile(userId, sm)
            }

            1 -> {
                println("이미지를 가져오겠습니다")
                userImageRepository.findImage(sm.profile!!, sm)
            }
        }

        return UserDto(sm.profile!!, sm.image!!)
    }

}

data class UserDto(
    val profile: Profile,
    val image: Image,
)

class UserProfileRepository {
    suspend fun findProfile(userId: Long, continuation: Continuation): Profile {
        delay(100L)
        return Profile()
    }
}

class Profile

class UserImageRepository {
    suspend fun findImage(profile: Profile, continuation: Continuation): Image {
        delay(100L)
        return Image()
    }
}

class Image

// 라벨을 갖고 있을 인터페이스, 점점 더 많은 기능이 추가될 것이다!
interface Continuation {

    suspend fun resumeWith(data: Any?)
}
