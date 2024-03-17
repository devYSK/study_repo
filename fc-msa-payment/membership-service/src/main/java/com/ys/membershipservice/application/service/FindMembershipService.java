package com.ys.membershipservice.application.service;

import org.springframework.transaction.annotation.Transactional;

import com.ys.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.ys.membershipservice.adapter.out.persistence.MembershipMapper;
import com.ys.membershipservice.application.port.in.FindMembershipCommand;
import com.ys.membershipservice.application.port.in.FindMembershipUseCase;
import com.ys.membershipservice.application.port.out.FindMembershipPort;
import com.ys.membershipservice.common.UseCase;
import com.ys.membershipservice.domain.Membership;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
@Transactional
public class FindMembershipService implements FindMembershipUseCase {

    private final FindMembershipPort findMembershipPort;

    private final MembershipMapper membershipMapper;
    @Override
    public Membership findMembership(FindMembershipCommand command) {
        MembershipJpaEntity entity = findMembershipPort.findMembership(new Membership.MembershipId(command.getMembershipId()));
        return membershipMapper.mapToDomainEntity(entity);
    }
}