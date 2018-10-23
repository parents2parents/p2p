package com.parents2parents;

import com.coxautodev.graphql.tools.SchemaParser;
import com.parents2parents.errors.SanitizedError;
import com.parents2parents.kindergarten.KindergartenRepository;
import com.parents2parents.kindergarten.KindergartenResolver;
import com.parents2parents.user.AuthContext;
import com.parents2parents.user.SigninResolver;
import com.parents2parents.user.User;
import com.parents2parents.user.UserRepository;
import com.parents2parents.vote.VoteRepository;
import com.parents2parents.vote.VoteResolver;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;


@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final KindergartenRepository KINDERGARTEN_REPOSITORY;

    private static final UserRepository userRepository;

    private static final VoteRepository voteRepository;

    static {
        //Change to `new MongoClient("mongodb://<host>:<port>/hackernews")`
        //if you don't have Mongo running locally on port 27017
        MongoDatabase mongo = new MongoClient().getDatabase("hackernews");
        KINDERGARTEN_REPOSITORY = new KindergartenRepository(mongo.getCollection("kindergartens"));
        userRepository = new UserRepository(mongo.getCollection("users"));
        voteRepository = new VoteRepository(mongo.getCollection("votes"));
    }


    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(KINDERGARTEN_REPOSITORY),
                        new Mutation(KINDERGARTEN_REPOSITORY, userRepository, voteRepository),
                        new SigninResolver(),
                        new KindergartenResolver(userRepository),
                        new VoteResolver(KINDERGARTEN_REPOSITORY, userRepository))
                .scalars(Scalars.dateTime)
                .build()
                .makeExecutableSchema();
    }

    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request
                .map(req -> req.getHeader("Authorization"))
                .filter(id -> !id.isEmpty())
                .map(id -> id.replace("Bearer ", ""))
                .map(userRepository::findById)
                .orElse(null);
        return new AuthContext(user, request, response);
    }

    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream()
                .filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e))
                .map(e -> e instanceof ExceptionWhileDataFetching ? new SanitizedError((ExceptionWhileDataFetching) e) : e)
                .collect(Collectors.toList());
    }
}