package com.cpm.account.repository;

import com.cpm.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "select user from UserEntity user " +
            " left join fetch user.accounts accounts" +
            " left join fetch accounts.transactions " +
            " where user.id=:userId")
    Optional<UserEntity> findUserById(@Param("userId") Long userId);
}
