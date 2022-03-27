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
        log.info("OPERATION " + op);
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

    public AuthSpecificationsBuilder with(
            String key, String operation, Object value) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        log.info("OPERATION " + op);

        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
//                boolean startWithAsterisk = prefix.contains("*");
//                boolean endWithAsterisk = suffix.contains("*");

//                if (startWithAsterisk && endWithAsterisk) {
//                    op = SearchOperation.CONTAINS;
//                } else if (startWithAsterisk) {
//                    op = SearchOperation.ENDS_WITH;
//                } else if (endWithAsterisk) {
//                    op = SearchOperation.STARTS_WITH;
//                }
            }
            log.info("RESULT OPERATION " + op);

            params.add(new SearchCriteria(key, op, value));
        }
        return this;
    }

//    public final AuthSpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
//        return with(null, key, operation, value, prefix, suffix);
//    }

//    public final AuthSpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
//        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
//        if (op != null) {
//            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
//                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
//                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
//
//                if (startWithAsterisk && endWithAsterisk) {
//                    op = SearchOperation.CONTAINS;
//                } else if (startWithAsterisk) {
//                    op = SearchOperation.ENDS_WITH;
//                } else if (endWithAsterisk) {
//                    op = SearchOperation.STARTS_WITH;
//                }
//            }
//            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
//        }
//        return this;
//    }

//    public Specification<Auth> build() {
//        if (params.size() == 0)
//            return null;
//
//        Specification<Auth> result = new AuthSpecification(params.get(0));
//
//        for (int i = 1; i < params.size(); i++) {
//            result = params.get(i).isOrPredicate()
//                    ? Specification.where(result).or(new AuthSpecification(params.get(i)))
//                    : Specification.where(result).and(new AuthSpecification(params.get(i)));
//        }
//
//        return result;
//    }
//
//    public final AuthSpecificationsBuilder with(AuthSpecification spec) {
//        params.add(spec.getCriteria());
//        return this;
//    }
//
//    public final AuthSpecificationsBuilder with(SpecSearchCriteria criteria) {
//        params.add(criteria);
//        return this;
//    }
public Specification<Auth> build() {
    if (params.size() == 0) {
        return null;
    }

    List<Specification> specs = params.stream()
//            .map(AuthSpecification::new)
            .map(AuthSpecification::new)
            .collect(Collectors.toList());

    Specification result = specs.get(0);

    for (int i = 1; i < params.size(); i++) {
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