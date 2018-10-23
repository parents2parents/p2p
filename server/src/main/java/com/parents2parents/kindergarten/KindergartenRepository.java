package com.parents2parents.kindergarten;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;


public class KindergartenRepository {


    private final MongoCollection<Document> kindergartens;

    public KindergartenRepository(MongoCollection<Document> kindergartens) {
        this.kindergartens = kindergartens;
    }

    public Kindergarten findById(String id) {
        Document doc = kindergartens.find(eq("_id", new ObjectId(id))).first();
        return kindergarten(doc);
    }

    public List<Kindergarten> getAllKindergartens(KindergartenFilter filter) {
        Optional<Bson> mongoFilter = Optional.ofNullable(filter).map(this::buildFilter);

        List<Kindergarten> allKindergartens = new ArrayList<>();
        for (Document doc : mongoFilter.map(kindergartens::find).orElseGet(kindergartens::find)) {
            allKindergartens.add(kindergarten(doc));
        }
        return allKindergartens;
    }

    //builds a Bson from a KindergartenFilter
    private Bson buildFilter(KindergartenFilter filter) {
        String descriptionPattern = filter.getDescriptionContains();
        String urlPattern = filter.getUrlContains();
        Bson descriptionCondition = null;
        Bson urlCondition = null;
        if (descriptionPattern != null && !descriptionPattern.isEmpty()) {
            descriptionCondition = regex("description", ".*" + descriptionPattern + ".*", "i");
        }
        if (urlPattern != null && !urlPattern.isEmpty()) {
            urlCondition = regex("url", ".*" + urlPattern + ".*", "i");
        }
        if (descriptionCondition != null && urlCondition != null) {
            return and(descriptionCondition, urlCondition);
        }
        return descriptionCondition != null ? descriptionCondition : urlCondition;
    }
    public void saveKindergarten(Kindergarten kindergarten) {
        Document doc = new Document();
        doc.append("url", kindergarten.getUrl());
        doc.append("description", kindergarten.getDescription());
        doc.append("postedBy", kindergarten.getUserId());
        kindergartens.insertOne(doc);
    }

    private Kindergarten kindergarten(Document doc) {
        return new Kindergarten(
                doc.get("_id").toString(),
                doc.getString("url"),
                doc.getString("description"),
                doc.getString("postedBy"));
    }
}