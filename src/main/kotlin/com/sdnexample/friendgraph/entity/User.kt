package com.sdnexample.friendgraph.entity

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.support.UUIDStringGenerator

@Node
data class User(

    @Id
    @GeneratedValue(UUIDStringGenerator::class)
    var userId: String?,

    var firstName: String,

    var lastName: String,

    @Relationship(type = "FRIEND_REQUEST", direction = Relationship.Direction.OUTGOING)
    var sentFriendRequestList: MutableList<FriendRequest> = mutableListOf(),

    @Relationship(type = "FRIEND_REQUEST", direction = Relationship.Direction.INCOMING)
    var receivedFriendRequestList: MutableList<FriendRequest> = mutableListOf(),

    var email: String

) {

    constructor(firstName: String, lastName: String, email: String) : this(null, firstName, lastName, mutableListOf(), mutableListOf(), email)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(userId='$userId', firstName='$firstName', lastName='$lastName', email='$email')"
    }

}