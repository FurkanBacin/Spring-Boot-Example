package com.furkanbacin.customer.repository;

import com.furkanbacin.customer.model.Customer;
import com.furkanbacin.customer.model.CustomerPage;
import com.furkanbacin.customer.model.CustomerSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CustomerCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public CustomerCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Customer> findAllWithFilters(CustomerPage customerPage, CustomerSearchCriteria customerSearchCriteria){

        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = criteriaQuery.from(Customer.class);
        Predicate predicate = getPredicate(customerSearchCriteria, customerRoot);
        criteriaQuery.where(predicate);
        setOrder(customerPage, criteriaQuery, customerRoot);

        TypedQuery<Customer> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(customerPage.getPageNumber() * customerPage.getPageSize());
        typedQuery.setMaxResults(customerPage.getPageSize());

        Pageable pageable = getPageable(customerPage);

        long customerCount = getCustomerCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(),pageable,customerCount);
    }

    private Predicate getPredicate(CustomerSearchCriteria customerSearchCriteria, Root<Customer> customerRoot) {

        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(customerSearchCriteria.getName())){
            predicates.add(
                    criteriaBuilder.like(customerRoot.get("name"),
                            "%" + customerSearchCriteria.getName() + "%")
            );

        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(CustomerPage customerPage, CriteriaQuery<Customer> criteriaQuery, Root<Customer> customerRoot) {

        if (customerPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(customerRoot.get(customerPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(customerRoot.get(customerPage.getSortBy())));
        }
    }

    private Pageable getPageable(CustomerPage customerPage) {
        Sort sort = Sort.by(customerPage.getSortDirection(), customerPage.getSortBy());
        return PageRequest.of(customerPage.getPageNumber(), customerPage.getPageSize(), sort);
    }

    private long getCustomerCount(Predicate predicate) {

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Customer> countRoot = countQuery.from(Customer.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
