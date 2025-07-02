package io.github.contractormicroservice.repository.contractor;

import io.github.contractormicroservice.model.entity.Contractor;
import io.github.contractormicroservice.model.entity.ContractorSearch;
import io.github.contractormicroservice.model.entity.Pagination;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractorJdbcRepository {

    Optional<Contractor> findByIdWithDetails(String id);

    Pagination searchContractors(ContractorSearch request, Integer page, Integer limit);

}

