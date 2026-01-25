package br.gov.mt.seplag.repository.base;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    default boolean existsBy(@Nullable final ID id, final Map<String, Object> properties) {
        return exists((root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            properties.forEach((key, value) ->
                predicates.add(builder.equal(root.get(key), value))
            );

            if (nonNull(id)) {
                predicates.add(builder.notEqual(root.get("id"), id));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        });
    }

}
