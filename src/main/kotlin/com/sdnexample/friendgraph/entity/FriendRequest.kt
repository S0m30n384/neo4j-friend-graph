package com.sdnexample.friendgraph.entity

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
data class FriendRequest(

    @Id
    @GeneratedValue
    var friendRequestId: Long?,

    /**
     * Represents the receiver in an OUTGOING relationship and the sender in an INCOMING relationship.
     */
    @TargetNode
    var friendRequestOtherNode: User

) {

    constructor(friendRequestOtherNode: User) : this(null, friendRequestOtherNode)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FriendRequest

        if (friendRequestId != other.friendRequestId) return false

        return true
    }

    override fun hashCode(): Int {
        return friendRequestId?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "FriendRequest(friendRequestId=$friendRequestId)"
    }


}