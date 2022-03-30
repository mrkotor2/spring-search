package com.project.siihomework.search;

import com.project.siihomework.model.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public AuthSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public AuthSpecificationsBuilder with(
            String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        log.info("OPERATION Multiple " + op);
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            log.info("RESULT OPERATION " + op);

            params.add(new SearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<Auth> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Auth>> specs = params.stream()
                .map(AuthSpecification::new)
                .collect(Collectors.toList());

        Specification<Auth> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) { // To loop through AND/OR cases
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }

        return result;
    }
}