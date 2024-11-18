package com.act.Gakos.repository;

import com.act.Gakos.entity.Role;
import com.act.Gakos.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByUsername(String username);

    Page<User> findAllByUsername(String username, Pageable pageable);



    @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    Optional<User> loadByUsername(String username);


    Optional<User> findByToken(String token);

    Page<User> findByRole(Role role, Pageable pageable);


    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.email) LIKE  LOWER(CONCAT('%', :searchTerm, '%') ) " +
            "OR LOWER(u.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.role) LIKE LOWER(concat('%', :searchTerm, '%') ) " +
            "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%') ) " +
            "ORDER BY u.createdAt ASC")
    Page<User> searchBySearchTerms(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Modifying
    @Query("UPDATE User u SET u.lastSeen = :lastSeen WHERE u.id = :userId")
    void updateLastSeen(@Param("userId") Integer userId, @Param("lastSeen") LocalDateTime lastSeen);

    @Query(value = "SELECT u.*, pp.* FROM users u " +
            "LEFT JOIN profile_pictures pp ON u.profile_picture_id = pp.id " +
            "ORDER BY u.created_at ASC", nativeQuery = true)
    Page<User> findAllUsersWithProfilePictures(Pageable pageable);


}
