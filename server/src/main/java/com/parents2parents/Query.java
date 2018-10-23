package com.parents2parents;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.parents2parents.kindergarten.Kindergarten;
import com.parents2parents.kindergarten.KindergartenFilter;
import com.parents2parents.kindergarten.KindergartenRepository;

import java.util.List;

public class Query implements GraphQLRootResolver {

    private final KindergartenRepository kindergartenRepository;

    public Query(KindergartenRepository kindergartenRepository) {
        this.kindergartenRepository = kindergartenRepository;
    }

    public List<Kindergarten> allKindergartens(KindergartenFilter filter) {
        return kindergartenRepository.getAllKindergartens(filter);
    }
}