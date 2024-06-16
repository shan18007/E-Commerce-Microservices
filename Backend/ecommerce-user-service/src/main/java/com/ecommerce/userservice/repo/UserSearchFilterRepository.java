package com.ecommerce.userservice.repo;

import com.ecommerce.common.dto.Filter;
import com.ecommerce.common.dto.UserSearchData;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserSearchFilterRepository {

    private final EntityManager entityManager;

    public UserSearchFilterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<UserSearchData> applyDynamicFilter(List<Filter> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserSearchData> query = criteriaBuilder.createQuery(UserSearchData.class);
        Root<User> root = query.from(User.class);

        List<Selection<?>> selections = new ArrayList<>();
        selections.add(root.get("userId"));
        selections.add(root.get("username"));
        selections.add(root.get("Role"));
        selections.add(root.get("active"));
        Join<User, UserInfo> userInfoJoin = root.join("userInfo", JoinType.INNER);
        selections.add(userInfoJoin.get("userInfoId"));
        selections.add(userInfoJoin.get("country"));
        selections.add(userInfoJoin.get("email"));
        selections.add(userInfoJoin.get("fullName"));
        selections.add(userInfoJoin.get("gender"));
        selections.add(userInfoJoin.get("lane"));
        selections.add(userInfoJoin.get("mobile"));
        selections.add(userInfoJoin.get("street"));
        selections.add(userInfoJoin.get("state"));
        selections.add(userInfoJoin.get("pincode"));
        selections.add(userInfoJoin.get("profileUrl"));
        query.multiselect(selections);

        List<Predicate> predicates = new ArrayList<>();
        buildPredicates(root, userInfoJoin, predicates, filters, criteriaBuilder);
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        query.where(finalPredicate);

        return entityManager.createQuery(query).getResultList();
    }

    private void buildPredicates(
            Root<?> root, Join<?, ?> userInfoJoin, List<Predicate> predicates,
            List<Filter> filters, CriteriaBuilder criteriaBuilder
    ) {
        for (Filter filter : filters) {
            String[] propertyPath = filter.getColumnName().split("\\.");
            Path<?> path = root;
            if (propertyPath.length > 1) {
                path = root;
                for (String property : propertyPath) {
                    path = path.get(property);
                }
            } else {
                // If property is not nested, check if it belongs to UserInfo join
                path = userInfoJoin.get(propertyPath[0]);
            }

            Object value = filter.getValue();
            if (value == null) {
                continue;
            }

            switch (filter.getCondition()) {
                case EQ:
                case DT_EQ:
                    predicates.add(criteriaBuilder.equal(path, value));
                    break;
                case LIKE:
                    if (value instanceof String) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) path), "%" + ((String) value).toLowerCase() + "%"));
                    } else {
                        throw new IllegalArgumentException("Invalid value type for condition LIKE");
                    }
                    break;
                case BOOLEAN:
                    if (value instanceof Boolean) {
                        predicates.add(criteriaBuilder.equal((Expression<String>) path, (Boolean) value));
                    } else {
                        throw new IllegalArgumentException("Invalid value type for condition BOOLEAN");
                    }
                    break;
                case GT:
                    if (value instanceof Comparable) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) value));
                    } else {
                        throw new IllegalArgumentException("Invalid value type for condition GT");
                    }
                    break;
                case LT:
                    if (value instanceof Comparable) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) value));
                    } else {
                        throw new IllegalArgumentException("Invalid value type for condition LT");
                    }
                    break;
                case IN:
                    if (value instanceof Collection) {
                        predicates.add(path.in((Collection<?>) value));
                    } else {
                        throw new IllegalArgumentException("Invalid value type for condition IN");
                    }
                    break;
                case NOT_IN:
                    if (value instanceof Collection) {
                        predicates.add(criteriaBuilder.not(path.in((Collection<?>) value)));
                    } else {
                        throw new IllegalArgumentException("Invalid value type for condition NOT_IN");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported condition type: " + filter.getCondition());
            }
        }
    }
}
