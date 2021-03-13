package com.sdnexample.friendgraph.repository

import com.sdnexample.friendgraph.entity.FriendRequest
import com.sdnexample.friendgraph.entity.User
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.Neo4jContainer
import org.testcontainers.junit.jupiter.Container
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataNeo4jTest
@ContextConfiguration(initializers = [UserRepositoryTest.Companion.Initializer::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Rollback(false)
class UserRepositoryTest(
    @Autowired val userRepository: UserRepository
) {

    companion object {

        private const val NEO4J_PORT = 7687

        @Container
        private val NEO4J_CONTAINER: FriendGraphTestNeo4jContainer =
            FriendGraphTestNeo4jContainer().withoutAuthentication().withExposedPorts(NEO4J_PORT)

        private const val NEO4J_URI_PROPERTY_NAME = "spring.neo4j.uri"

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                NEO4J_CONTAINER.start()
                val testPropertyValues = TestPropertyValues.of("${NEO4J_URI_PROPERTY_NAME}=${NEO4J_CONTAINER.boltUrl}")
                testPropertyValues.applyTo(applicationContext.environment)
            }
        }

        const val BILBO_EMAIL = "bilbo.baggins@test.com"
        const val FRODO_EMAIL = "frodo.baggins@test.com"
        const val SAM_EMAIL = "samwise.gamgee@test.com"

    }

    class FriendGraphTestNeo4jContainer : Neo4jContainer<FriendGraphTestNeo4jContainer>("neo4j:4.2.1")

    @Test
    @Order(1)
    fun `Assert that the testcontainer is running`() {
        assertTrue { NEO4J_CONTAINER.isRunning }
    }

    @Test
    @Order(2)
    fun `Save three users`() {
        val bilbo = User("Bilbo", "Baggins", BILBO_EMAIL)
        val frodo = User("Frodo", "Baggins", FRODO_EMAIL)
        val sam = User("Samwise", "Gamgee", SAM_EMAIL)
        userRepository.saveAll(mutableListOf(bilbo, frodo, sam))
    }

    @Test
    @Order(3)
    fun `Should save a FriendRequest from Frodo to Sam`() {
        val frodo = userRepository.findByEmailWithFriendRequests(FRODO_EMAIL)
        val sam = userRepository.findByEmailWithFriendRequests(SAM_EMAIL)
        val sentFriendRequest = FriendRequest(sam)
        val receivedFriendRequest = FriendRequest(frodo)
        frodo.sentFriendRequestList.add(sentFriendRequest)
        sam.receivedFriendRequestList.add(receivedFriendRequest)
        userRepository.save(frodo)
        userRepository.save(sam)
    }

    @Test
    @Order(4)
    fun `Should save a FriendRequest from Frodo to Bilbo`() {
        val frodo = userRepository.findByEmailWithFriendRequests(FRODO_EMAIL)
        val bilbo = userRepository.findByEmailWithFriendRequests(BILBO_EMAIL)
        val sentFriendRequest = FriendRequest(bilbo)
        val receivedFriendRequest = FriendRequest(frodo)
        frodo.sentFriendRequestList.add(sentFriendRequest)
        bilbo.receivedFriendRequestList.add(receivedFriendRequest)
        userRepository.save(frodo)
        userRepository.save(bilbo)
    }

    @Test
    @Order(5)
    fun `Expecting all users to have all their sent and received friend requests`() {
        val frodo = userRepository.findByEmailWithFriendRequests(FRODO_EMAIL)
        val sam = userRepository.findByEmailWithFriendRequests(SAM_EMAIL)
        val bilbo = userRepository.findByEmailWithFriendRequests(BILBO_EMAIL)
        // in all cases the assertion below is successful because the latest save was between Frodo and Bilbo.
        assertEquals(1, bilbo.receivedFriendRequestList.size, "Bilbo doesn't have the expected amount of received friend requests!")
        // in SOME cases the following assertion fails because the Frodo -> Sam friend request got deleted when the Frodo -> Bilbo friend request was created.
        assertEquals(1, sam.receivedFriendRequestList.size, "Sam doesn't have the expected amount of received friend requests!")
        assertEquals(2, frodo.sentFriendRequestList.size, "Frodo doesn't have the expected amount of sent friend requests!")
    }


}