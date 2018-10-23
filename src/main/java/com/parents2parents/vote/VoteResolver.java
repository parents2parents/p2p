package com.parents2parents.vote;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.parents2parents.kindergarten.Kindergarten;
import com.parents2parents.kindergarten.KindergartenRepository;
import com.parents2parents.user.User;
import com.parents2parents.user.UserRepository;

public class VoteResolver implements GraphQLResolver<Vote> {

    private final KindergartenRepository kindergartenRepository;
    private final UserRepository userRepository;

    public VoteResolver(KindergartenRepository kindergartenRepository, UserRepository userRepository) {
        this.kindergartenRepository = kindergartenRepository;
        this.userRepository = userRepository;
    }

    public User user(Vote vote) {
        return userRepository.findById(vote.getUserId());
    }

    public Kindergarten kindergarten(Vote vote) {
        return kindergartenRepository.findById(vote.getKindergartenId());
    }
}
