package com.yscorp.web1.interfaces

import com.yscorp.web1.common.Response
import com.yscorp.web1.domain.MemberService
import com.yscorp.web1.domain.dto.MemberInput
import com.yscorp.web1.domain.dto.MemberResponse
import jakarta.annotation.PostConstruct
import org.reactivestreams.Publisher
import org.springframework.graphql.data.ArgumentValue
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink


@Controller
class MemberController(
    val service: MemberService,
) {

    private lateinit var memberStream: FluxSink<Response>
    private lateinit var memberPublisher: ConnectableFlux<Response>

    @PostConstruct
    fun initSubscription() {
        val publisher: Flux<Response> =
            Flux.create { emitter: FluxSink<Response> ->
                memberStream = emitter
            }
        memberPublisher = publisher.publish()
        memberPublisher!!.connect()
    }

    @QueryMapping("getMember")
    fun getMember(@Argument memberId: Long): MemberResponse {
        return service.fetchMember(memberId)
    }

    @MutationMapping("addMember")
    fun addMember(memberInput: ArgumentValue<MemberInput>): Response {
        val addMemberResponse: Response = service.addMember(memberInput)
        memberStream!!.next(addMemberResponse)
        return addMemberResponse
    }

    @MutationMapping("removeMember")
    fun removeMember(@Argument memberId: Long): Response {
        val removeMemberResponse: Response = service.removeMember(memberId)
        memberStream!!.next(removeMemberResponse)
        return removeMemberResponse
    }

    @SubscriptionMapping("memberSubscription")
    fun addMemberSubscription(): Publisher<Response> {
        return memberPublisher
    }

}