package com.parents2parents.kindergarten;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.parents2parents.user.User;
import com.parents2parents.user.UserRepository;

public class KindergartenResolver implements GraphQLResolver<Kindergarten> {

    private final UserRepository userRepository;

    public KindergartenResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User postedBy(Kindergarten kindergarten) {
        if (kindergarten.getUserId() == null) {
            return null;
        }
        return userRepository.findById(kindergarten.getUserId());
    }
}