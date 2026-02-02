package com.Project.LinkedIn.Connection_Service.Repository;

import com.Project.LinkedIn.Connection_Service.Entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepositories extends Neo4jRepository<Person,Long> {

    Optional<Person> getByName(String name);

    // Remove the arrow in the MATCH to find connections in either direction
    @Query("MATCH (personA:Person)-[:CONNECTED_TO]-(personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnection(Long userId);

    @Query("MATCH (personA:Person)-[:CONNECTED_TO*2]-(personB:Person) " +
            "WHERE personA.userId = $userId AND personB.userId <> $userId " +          //<> not equals
            "RETURN DISTINCT personB")
    List<Person> getSecondDegreeConnection(Long userId);

    @Query("MATCH (personA:Person)-[:CONNECTED_TO*3]->(personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getThirdDegreeConnection(Long userId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId,Long receiverId);

    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId,Long receiverId);

//    @Query("MATCH (p1:Person), (p2:Person) " +
//            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
//            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
//    void addConnectionRequest(Long senderId,Long receiverId);
//
//    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person)" +
//            "WHERE p1.userId =  $senderId AND p2.userId = $receiverId " +
//            "DELETE r " +
//            "CREATE (p1)-[:CONNECTED_TO]->(p2)")
//    void AcceptRequest(Long senderId, Long receiverId);

    // 1. Updated: Use MERGE to ensure Persons exist before creating the relationship
    @Query("MERGE (p1:Person {userId: $senderId}) " +
            "MERGE (p2:Person {userId: $receiverId}) " +
            "MERGE (p1)-[:REQUESTED_TO]->(p2)")                   // we used merge in place of match bcoz if node is not present then create and if exist will not create
    void addConnectionRequest(Long senderId, Long receiverId);

    // 2. Updated: Ensure nodes exist and transition the relationship
    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r " +
            "MERGE (p1)-[:CONNECTED_TO]->(p2)")
    void AcceptRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r")
    void RejectConnectionRequest(Long senderId, Long receiverId);
}
