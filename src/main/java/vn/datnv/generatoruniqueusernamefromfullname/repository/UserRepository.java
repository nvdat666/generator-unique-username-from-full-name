package vn.datnv.generatoruniqueusernamefromfullname.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.datnv.generatoruniqueusernamefromfullname.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    
    @Query(nativeQuery = true, value = "SELECT username FROM users where username REGEXP :regexUsername  order by username desc limit 1")
    String findExistUsernameByRegex(String regexUsername);
    
}
