package com.jazasoft.mtdbapp.repository;

import com.jazasoft.mtdbapp.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mdzahidraza on 22/07/17.
 */
public interface TUserRepository extends JpaRepository<TUser, Long> {
}
