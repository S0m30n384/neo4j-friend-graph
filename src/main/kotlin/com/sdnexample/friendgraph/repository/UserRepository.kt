package com.sdnexample.friendgraph.repository

import com.sdnexample.friendgraph.entity.User
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query

interface UserRepository : Neo4jRepository<User, String> {

    @Query(
        "MATCH (user:User{email: \$email}) " +
                "OPTIONAL MATCH path=(user)-[:FRIEND_REQUEST*1]-(friend:User) " +
                "WITH user, friend, relationships(path) as friend_request_relationships " +
                "UNWIND CASE WHEN friend_request_relationships is null THEN [null] ELSE friend_request_relationships " +
                "END AS friend_requests " +
                "RETURN user, collect(friend_requests), collect(distinct(friend))"
    )
    fun findByEmailWithFriendRequests(email: String): User

}