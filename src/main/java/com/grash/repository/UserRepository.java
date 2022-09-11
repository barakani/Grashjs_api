package com.grash.repository;
import com.grash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    User findUserByEmail(String email);

    @Transactional
    void deleteByUsername(String username);

    boolean existsByEmail(String email);
}
