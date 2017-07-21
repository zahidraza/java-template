package com.jazasoft.mtdb.repository;

import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by mdzahidraza on 28/06/17.
 */
public interface UrlInterceptorRepository extends JpaRepository<UrlInterceptor,Long> {

    List<UrlInterceptor> findByUrl(String url);

    List<UrlInterceptor> findByCompanyAndUrl(Company company, String url);

    List<UrlInterceptor> findByCompany(Company company);

    List<UrlInterceptor> findByUrlContaining(String url);

    Optional<UrlInterceptor> findOneByCompanyAndId(Company company, Long id);
}
