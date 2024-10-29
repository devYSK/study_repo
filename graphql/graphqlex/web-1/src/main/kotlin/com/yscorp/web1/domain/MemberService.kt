package com.yscorp.web1.domain

import com.yscorp.web1.common.PageInput
import com.yscorp.web1.common.Response
import com.yscorp.web1.common.Status
import com.yscorp.web1.domain.dto.MemberInput
import com.yscorp.web1.domain.dto.MemberResponse
import com.yscorp.web1.domain.dto.MemberSearchResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.ArgumentValue
import org.springframework.stereotype.Service

@Service
class MemberService @Autowired constructor(private val repository: MemberRepository) {

    fun addMember(memberInputArgValue: ArgumentValue<MemberInput>): Response {
        val member = Member()
        var savedMember = Member()

        if (!memberInputArgValue.isOmitted && memberInputArgValue.isPresent) {
            val memberInput: MemberInput? = memberInputArgValue.value()
            member.firstName = memberInput?.firstName
            member.lastName = memberInput?.lastName
            member.contact = memberInput?.contact
            member.type = memberInput?.type!!

            savedMember = repository.saveAndFlush(member)
        }
        val createdString = "Member Created: ${member.firstName} ${member.lastName}"

        return Response(Status.SUCCESS, savedMember.id, createdString)
    }

    fun getMembers(type: MemberType?): List<MemberResponse> {
        val members: List<Member> = if (type == null) {
            repository.findAll()
        } else {
            repository.findByType(type)
        }

        println("get member Size : ${members.size}")
        return members.map { MemberResponse(it) }
    }

    fun getSearchResult(text: String?): List<MemberSearchResult> {
        val members = repository.fetchMembersByName(text!!)

        println("text : ${text}, size : ${members.size}")
        if (members.isEmpty()) {
            return emptyList()
        }

        return members.map { MemberSearchResult(it) }
    }

    fun getPaginatedMembers(pageInput: PageInput): List<MemberResponse> {
        return repository.findAll(PageRequest.of(pageInput.offset, pageInput.limit))
            .map {
                MemberResponse(it)
            }.toList()
    }

    fun fetchMember(memberId: Long): MemberResponse {
        val member = repository.getReferenceById(memberId)
        return MemberResponse(member)
    }

    fun removeMember(memberId: Long): Response {
        val member = repository.getReferenceById(memberId)
        repository.delete(member)
        val removedString = "Member Removed: ${member.firstName} ${member.lastName}"

        return Response(Status.SUCCESS, member.id, removedString)
    }
}
