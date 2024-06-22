package com.vinn.vinnet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinn.vinnet.model.Friendship;
import com.vinn.vinnet.model.User;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserIdAndFriendId(long userId, long friendId);
//    @Query("SELECT f.friend FROM Friendship f JOIN f.friend WHERE f.user.id = :userId AND f.status = :status")
    List<Friendship> findByUserIdAndStatus(@Param("userId") long userId, @Param("status") String status);
    @Query("SELECT f.friend FROM Friendship f JOIN f.friend WHERE f.user.id = :userId")
    List<User> findByUserId(@Param("userId") long userId);
    
    @Query("SELECT f.friend FROM Friendship f " +
           "INNER JOIN Friendship f2 ON f.friend.id = f2.friend.id " +
           "WHERE f.user.id = :userId1 AND f2.user.id = :userId2")
    List<User> findMutualFriends(@Param("userId1") long userId1, @Param("userId2") long userId2);
	List<Friendship> findByFriendIdAndStatus(long userId, String name);    
}
