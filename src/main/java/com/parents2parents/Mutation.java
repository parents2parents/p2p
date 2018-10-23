package com.parents2parents;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.parents2parents.kindergarten.Kindergarten;
import com.parents2parents.kindergarten.KindergartenRepository;
import com.parents2parents.user.AuthContext;
import com.parents2parents.user.AuthData;
import com.parents2parents.user.SigninPayload;
import com.parents2parents.user.User;
import com.parents2parents.user.UserRepository;
import com.parents2parents.vote.Vote;
import com.parents2parents.vote.VoteRepository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;

public class Mutation implements GraphQLRootResolver {

    private final KindergartenRepository kindergartenRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public Mutation(KindergartenRepository kindergartenRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.kindergartenRepository = kindergartenRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Kindergarten createKindergarten(String url, String description, DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        Kindergarten newKindergarten = new Kindergarten(url, description, context.getUser().getId());
        kindergartenRepository.saveKindergarten(newKindergarten);
        return newKindergarten;
    }

    public User createUser(String name, AuthData auth) {
        User newUser = new User(name, auth.getEmail(), auth.getPassword());
        return userRepository.saveUser(newUser);
    }

    public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByEmail(auth.getEmail());
        if (user.getPassword().equals(auth.getPassword())) {
            //TODO dafna the token in this example is just the user id. In reality, it should be a JWT or similar.
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }

    public Vote createVote(String kindergartenId, String userId) {
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
        return voteRepository.saveVote(new Vote(now, userId, kindergartenId));
    }
}
