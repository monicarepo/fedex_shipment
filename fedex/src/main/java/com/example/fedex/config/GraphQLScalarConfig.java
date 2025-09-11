package com.example.fedex.config;

import com.example.fedex.entity.GraphQLRole;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLScalarConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(GraphQLScalarType.newScalar()
                        .name("Role")
                        .description("User role enum")
                        .coercing(new Coercing<GraphQLRole, String>() {
                            @Override
                            public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                                if (dataFetcherResult instanceof GraphQLRole) {
                                    return ((GraphQLRole) dataFetcherResult).name();
                                }
                                throw new CoercingSerializeException("Not a valid Role");
                            }

                            @Override
                            public GraphQLRole parseValue(Object input) throws CoercingParseValueException {
                                if (input instanceof String) {
                                    try {
                                        return GraphQLRole.valueOf(((String) input).toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        throw new CoercingParseValueException("Not a valid Role: " + input);
                                    }
                                }
                                throw new CoercingParseValueException("Not a valid Role");
                            }

                            @Override
                            public GraphQLRole parseLiteral(Object input) throws CoercingParseLiteralException {
                                if (input instanceof String) {
                                    try {
                                        return GraphQLRole.valueOf(((String) input).toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        throw new CoercingParseLiteralException("Not a valid Role: " + input);
                                    }
                                }
                                throw new CoercingParseLiteralException("Not a valid Role");
                            }
                        })
                        .build());
    }
}